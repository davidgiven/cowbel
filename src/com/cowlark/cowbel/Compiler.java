/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING.BSD in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel;

import java.util.Collection;
import java.util.Collections;
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
import com.cowlark.cowbel.types.HasInterfaces;
import com.cowlark.cowbel.types.InterfaceType;
import com.cowlark.cowbel.types.Type;

public class Compiler implements HasInterfaces
{
	public static Compiler Instance;
	
	private CompilerListener _listener;
	private Backend _backend;
	private Location _input;
	private TypeContext _rootTypeContext;
	private Function _mainFunction;
	private TreeSet<Constructor> _constructors = new TreeSet<Constructor>();
	private TreeSet<InterfaceType> _interfaces = new TreeSet<InterfaceType>();
	private FunctionScopeConstructorNode _ast;

	private Visitor _record_type_definitions_visitor =
		new RecordTypeDefinitionsVisitor();
	private Visitor _define_interfaces_visitor =
		new DefineInterfacesVisitor();
	private Visitor _record_variable_declarations_visitor =
		new RecordVariableDeclarationsVisitor();
	private Visitor _resolve_variable_references_visitor =
		new ResolveVariableReferencesVisitor();
	private Visitor _assign_functions_to_scopes_visitor =
		new AssignFunctionsToScopesVisitor();
	private Visitor _classify_scope_kinds_visitor =
		new ClassifyScopeKindsVisitor();
	private Visitor _assign_constructors_to_scopes_visitor =
		new AssignConstructorsToScopesVisitor();
	private Visitor _collect_constructors_visitor =
		new CollectConstructorsVisitor(_constructors);
	private Visitor _assign_variables_to_constructors_visitor =
		new AssignVariablesToConstructorsVisitor();

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
		return Collections.unmodifiableCollection(
				FunctionTemplate.getInstantiatedFunctions().values());
	}
	
	@Override
	public Collection<InterfaceType> getInterfaces()
	{
	    return Collections.unmodifiableCollection(_interfaces);
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
			_mainFunction = template.instantiate(mainnode, null);
			
			Function function = FunctionTemplate.getNextPendingFunction();
			while (function != null)
			{
				do
				{
					if (function == null)
						break;
					
					record_type_declarations(function);
					record_variable_declarations(function);
					check_types(function); /* will instantiate new functions */
					
					function = FunctionTemplate.getNextPendingFunction();
				}
				while (function != null);
				
				/* We've processed all pending function instantiations. Now
				 * instantiate any functions that are used as methods. */
				
				for (InterfaceType itype : _interfaces)
					itype.instantiateMethods();
			
				function = FunctionTemplate.getNextPendingFunction();
			}
		}

		_listener.onSymbolTableAnalysisEnd();
		
		/* Construct basic blocks and IR representation. */
		
		_listener.onBasicBlockAnalysisBegin();
		
		visit(_classify_scope_kinds_visitor);
		visit(_assign_constructors_to_scopes_visitor);
		visit(_collect_constructors_visitor);
		visit(_assign_variables_to_constructors_visitor);
		
		for (Function f : getFunctions())
			f.buildBasicBlocks();
		
		_listener.onBasicBlockAnalysisEnd();
		
		/* Code generation. */
		
		_listener.onCodeGenerationBegin();
		_backend.setMainFunction(_mainFunction);
		_backend.prologue();
		for (InterfaceType i : _interfaces)
			_backend.visit(i);
		for (Constructor c : _constructors)
			_backend.visit(c);
		for (Function f : getFunctions())
			_backend.compileFunction(f);
		_backend.epilogue();
		_listener.onCodeGenerationEnd();
	}

	public void visit(BasicBlockVisitor visitor)
	{
		TreeSet<BasicBlock> pending = new TreeSet<BasicBlock>();
		TreeSet<BasicBlock> seen = new TreeSet<BasicBlock>();
		
		for (Function function : getFunctions())
		{
			pending.add(function.getEntryBB());
			seen.add(function.getEntryBB());
			
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
	
	public void visit(Visitor visitor) throws CompilationException
	{
		for (Function f : getFunctions())
			f.getBody().visit(visitor);
	}
	
	public void dumpAnnotatedAST()
	{
		Visitor visitor = new ASTDumperVisitor();
		
		for (Function function : getFunctions())
		{
			System.out.println();
			System.out.println(function.toString());
			
			try
			{
				function.getNode().visit(visitor);
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
		
		for (Function function : getFunctions())
		{
			System.out.println(function.toString());
			
			pending.add(function.getEntryBB());
			seen.add(function.getEntryBB());
			
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
	
	public void dumpInterfaces()
	{
		for (InterfaceType i : _interfaces)
		{
			System.out.println(i.toString());
			i.dumpDetails();
			System.out.println("");
		}
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
			Type variabletype = pdn.getVariableTypeNode().getType();
			
			Variable v = new Variable(pdn, variablename, variabletype);
			v.setParameter(true);
			v.setOutputParameter(isOutputParameter);
			v.setScope(body);
			body.addSymbol(v);
			pdn.setSymbol(v);
		}
	}
	
	private void record_type_declarations(Function function)
			throws CompilationException
	{
		FunctionDefinitionNode node = (FunctionDefinitionNode) function.getNode();
		FunctionScopeConstructorNode body = node.getFunctionBody();
		body.visit(_record_type_definitions_visitor);
		body.visit(_define_interfaces_visitor);				
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
		
		body.visit(_assign_functions_to_scopes_visitor);
		body.visit(_record_variable_declarations_visitor);
		body.visit(_resolve_variable_references_visitor);
	}
	
	private void check_types(Function function) throws CompilationException
	{
		function.getBody().checkTypes();
	}

	public void addInterface(InterfaceType itype)
	{
		_interfaces.add(itype);
	}
}
