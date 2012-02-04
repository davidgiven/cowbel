/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;
import com.cowlark.cowbel.ast.Visitor;
import com.cowlark.cowbel.ast.nodes.AbstractScopeConstructorNode;
import com.cowlark.cowbel.ast.nodes.FunctionDefinitionNode;
import com.cowlark.cowbel.ast.nodes.FunctionHeaderNode;
import com.cowlark.cowbel.ast.nodes.FunctionScopeConstructorNode;
import com.cowlark.cowbel.ast.nodes.IdentifierListNode;
import com.cowlark.cowbel.ast.nodes.IdentifierNode;
import com.cowlark.cowbel.ast.nodes.Node;
import com.cowlark.cowbel.ast.nodes.ParameterDeclarationListNode;
import com.cowlark.cowbel.ast.nodes.ParameterDeclarationNode;
import com.cowlark.cowbel.ast.nodes.TypeListNode;
import com.cowlark.cowbel.backend.Backend;
import com.cowlark.cowbel.errors.CompilationException;
import com.cowlark.cowbel.errors.FailedParseException;
import com.cowlark.cowbel.instructions.Instruction;
import com.cowlark.cowbel.instructions.InstructionVisitor;
import com.cowlark.cowbel.parser.core.FailedParse;
import com.cowlark.cowbel.parser.core.Location;
import com.cowlark.cowbel.parser.core.MutableLocation;
import com.cowlark.cowbel.parser.core.ParseResult;
import com.cowlark.cowbel.parser.parsers.Parser;
import com.cowlark.cowbel.symbols.Variable;
import com.cowlark.cowbel.types.FunctionType;
import com.cowlark.cowbel.types.Type;

public class Compiler
{
	public static Compiler Instance;
	
	private CompilerListener _listener;
	private Backend _backend;
	private Location _input;
	private TypeContext _rootTypeContext;
	private Function _mainFunction;
	private TreeMap<String, Function> _functions = new TreeMap<String, Function>();
	private TreeMap<String, Function> _newFunctions = new TreeMap<String, Function>();
	private TreeSet<Constructor> _constructors = new TreeSet<Constructor>();
	private FunctionScopeConstructorNode _ast;
	
	public Compiler()
    {
		_listener = new CompilerListenerAdapter();
		Instance = this;
    }
	
	public void setListener(CompilerListener listener)
	{
		_listener = listener;
	}
	
	public void setInput(Location input)
	{
		_input = input;
	}
	
	public void setBackend(Backend backend)
	{
		_backend = backend;
	}
	
	public AbstractScopeConstructorNode getAst()
	{
		return (AbstractScopeConstructorNode) _ast;
	}
	
	public Collection<Constructor> getConstructors()
	{
	    return Collections.unmodifiableSet(_constructors);
    }
	
	public Collection<Function> getFunctions()
	{
		return Collections.unmodifiableCollection(_functions.values());
	}
	
	public void compile() throws CompilationException
	{
		_listener.onParseBegin();
		ParseResult pr = Parser.ProgramParser.parse(_input);
		_listener.onParseEnd();
		
		if (pr.failed())
		{
			FailedParse fp = (FailedParse) pr;
			throw new FailedParseException(fp);
		}

		_ast = (FunctionScopeConstructorNode) pr;
		
		_rootTypeContext = new TypeContext(_ast, null);
		for (Type t : S.ROOT_TYPES)
		{
			_rootTypeContext.addType(
					IdentifierNode.createIdentifier(t.getCanonicalTypeName()),
					t);
		}
		
		/* Analyse symbol tables. */
		
		_listener.onSymbolTableAnalysisBegin();
		{
			Location loc = new Location("", "<internal>");
			Location mainname = new Location("<main>", "<internal>");
			MutableLocation mainnameend = new MutableLocation(mainname);
			mainnameend.advance(6);
			
			FunctionDefinitionNode mainnode =
				new FunctionDefinitionNode(loc, loc,
					new FunctionHeaderNode(
						loc, loc,
						new IdentifierNode(mainname, mainnameend),
						new IdentifierListNode(mainname, mainnameend),
						new ParameterDeclarationListNode(loc, loc),
						new ParameterDeclarationListNode(loc, loc)
					),
					_ast
				);
			
			FunctionTemplate template = new FunctionTemplate(_rootTypeContext, mainnode);
			_mainFunction = getFunctionInstance(mainnode, null, template);
			
			while (!_newFunctions.isEmpty())
			{
				Map.Entry<String, Function> e = _newFunctions.pollFirstEntry();
				String signature = e.getKey();
				Function function = e.getValue();
				_functions.put(signature, function);
				
				record_variable_declarations(function);
				check_types(function);
				resolve_scopes_and_constructors(function);
			}
		}
		_listener.onSymbolTableAnalysisEnd();
		
		/* Construct basic blocks and IR representation. */
		
		_listener.onBasicBlockAnalysisBegin();
		
		for (Function f : _functions.values())
			f.buildBasicBlocks();
		
		_listener.onBasicBlockAnalysisEnd();
		
		/* Code generation. */
		
		_listener.onCodeGenerationBegin();
		_backend.prologue();
		for (Constructor c : _constructors)
			_backend.visit(c);
		for (Function f : _functions.values())
			_backend.compileFunction(f);
		_backend.epilogue();
		_listener.onCodeGenerationEnd();
	}

	public void visit(BasicBlockVisitor visitor)
	{
		TreeSet<BasicBlock> pending = new TreeSet<BasicBlock>();
		TreeSet<BasicBlock> seen = new TreeSet<BasicBlock>();
		
		for (Map.Entry<String, Function> e : _functions.entrySet())
		{
			Function f = e.getValue();
			pending.add(f.getEntryBB());
			seen.add(f.getEntryBB());
			
			while (!pending.isEmpty())
			{
				BasicBlock bb = pending.iterator().next();
				pending.remove(bb);
		
				for (BasicBlock b : bb.getDestinationBlocks())
				{
					if (!seen.contains(b))
					{
						seen.add(b);
						pending.add(b);
					}
				}
				
				visitor.visit(bb);
			}
		}
	}
	
	private class BasicBlockToInstructionAdapter extends BasicBlockVisitor
	{
		private InstructionVisitor _visitor;
		
		public BasicBlockToInstructionAdapter(InstructionVisitor visitor)
        {
			_visitor = visitor;
        }
		
		@Override
		public void visit(BasicBlock bb)
		{
			bb.visit(_visitor);
		}
	}
	
	public void visit(InstructionVisitor visitor)
	{
		BasicBlockToInstructionAdapter adapter = new BasicBlockToInstructionAdapter(visitor);
		visit(adapter);
	}
	
	public void dumpAnnotatedAST()
	{
		Visitor visitor = new ASTDumperVisitor();
		
		for (Map.Entry<String, Function> e : _functions.entrySet())
		{
			Function f = e.getValue();
			System.out.println();
			System.out.println(f.toString());
			
			try
			{
				f.getNode().visit(visitor);
			}
			catch (CompilationException ex)
			{
				assert(false);
			}
		}
	}
	
	public void dumpBasicBlocks()
	{
		TreeSet<BasicBlock> pending = new TreeSet<BasicBlock>();
		TreeSet<BasicBlock> seen = new TreeSet<BasicBlock>();
		
		for (Map.Entry<String, Function> e : _functions.entrySet())
		{
			Function f = e.getValue();
			System.out.println(f.toString());
			
			pending.add(f.getEntryBB());
			seen.add(f.getEntryBB());
			
			while (!pending.isEmpty())
			{
				BasicBlock bb = pending.iterator().next();
				pending.remove(bb);
		
				for (BasicBlock b : bb.getDestinationBlocks())
				{
					if (!seen.contains(b))
					{
						seen.add(b);
						pending.add(b);
					}
				}
				
				System.out.print("  ");
				System.out.print(bb.toString());
				System.out.print(" ");
				System.out.println(bb.description());
				
				for (Instruction insn : bb.getInstructions())
				{
					System.out.print("    ");
					System.out.println(insn.toString());
				}
			}
		}
	}
	
	public void dumpConstructors()
	{
		for (Constructor c : _constructors)
		{
			System.out.println(c.toString());
			c.dumpDetails();
			System.out.println("");
		}
	}
	
	private static ASTCopyVisitor astCopyVisitor = new ASTCopyVisitor();
	
	/** Returns the function instance for the given template with the given
	 * type assignments. If no suitable function has already been instantiated,
	 * instantiate a new one and add it to the compiler's pending list.
	 */
	
	public Function getFunctionInstance(Node node,
			TypeListNode typeassignments, FunctionTemplate template)
				throws CompilationException
	{
		TypeContext tc = template.createTypeContext(node, typeassignments);
		String signature = tc.getSignature();
		signature += " ";
		signature += template.getNode().locationAsString();

		Function function = _functions.get(signature);
		if (function != null)
			return function;
		function = _newFunctions.get(signature);
		if (function != null)
			return function;
		
		/* Deep-copy the AST tree so the version the function gets can be
		 * annotated without affecting any other instantiations. */
		
		template.getNode().visit(astCopyVisitor);
		FunctionDefinitionNode ast = (FunctionDefinitionNode) astCopyVisitor.getResult();
		
		ast.setParent(template.getNode().getParent());
		ast.setTypeContext(tc);
		
		function = new Function(signature, ast);
		FunctionType type = (FunctionType) ast.getFunctionHeader().calculateFunctionType();
		function.setType(type);
		
		_newFunctions.put(signature, function);
		
		return function;
	}
	
	private void add_parameters(
			TypeContext typecontext,
			FunctionDefinitionNode node,
			ParameterDeclarationListNode pdln, boolean isOutputParameter)
			throws CompilationException
	{
		FunctionScopeConstructorNode body = node.getFunctionBody();

		for (Node n : pdln)
		{
			ParameterDeclarationNode pdn = (ParameterDeclarationNode) n;
			IdentifierNode variablename = pdn.getVariableName();
			Type variabletype = pdn.getVariableTypeNode().calculateType();
			
			Variable v = new Variable(pdn, variablename, variabletype);
			v.setParameter(true);
			v.setOutputParameter(isOutputParameter);
			v.setScope(body);
			body.addSymbol(v);
			pdn.setSymbol(v);
		}
	}
	
	private void record_variable_declarations(Function function)
			throws CompilationException
	{
		TypeContext typecontext = function.getTypeContext();
		FunctionDefinitionNode node = (FunctionDefinitionNode) function.getNode();
		FunctionScopeConstructorNode body = node.getFunctionBody();
		body.setFunction(function);
		function.setScope(body);
		
		/* Add function parameters to its scope. */
		
		ParameterDeclarationListNode pdln = node.getFunctionHeader().getInputParametersNode();
		add_parameters(typecontext, node, pdln, false);

		/* Add function return parameters to scope. */
		
		pdln = node.getFunctionHeader().getOutputParametersNode();
		add_parameters(typecontext, node, pdln, true);
		
		/* Add any variable definitions to scope. */
		
		body.visit(new RecordVariableDeclarationsVisitor());
		body.visit(new ResolveVariableReferencesVisitor());
	}
	
	private void check_types(Function function) throws CompilationException
	{
		function.getBody().checkTypes();
	}
	
	private Visitor _assign_functions_to_scopes_visitor =
		new AssignFunctionsToScopesVisitor();
	private Visitor _assign_constructors_to_scopes_visitor =
		new AssignConstructorsToScopesVisitor(_constructors);
	private Visitor _assign_variables_to_constructors_visitor =
		new AssignVariablesToConstructorsVisitor();
	private void resolve_scopes_and_constructors(Function function)
			throws CompilationException
	{
		FunctionScopeConstructorNode body = function.getBody();
	
		body.visit(_assign_functions_to_scopes_visitor);
		body.visit(_assign_constructors_to_scopes_visitor);
		body.visit(_assign_variables_to_constructors_visitor);
	}
}
