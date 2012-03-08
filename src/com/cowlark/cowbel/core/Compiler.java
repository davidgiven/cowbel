/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING.BSD in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel.core;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Set;
import java.util.TreeSet;
import com.cowlark.cowbel.ASTDumperVisitor;
import com.cowlark.cowbel.AssignFunctionsToScopesVisitor;
import com.cowlark.cowbel.ast.ASTVisitor;
import com.cowlark.cowbel.ast.AbstractExpressionNode;
import com.cowlark.cowbel.ast.AbstractScopeConstructorNode;
import com.cowlark.cowbel.ast.FunctionDefinitionNode;
import com.cowlark.cowbel.ast.FunctionHeaderNode;
import com.cowlark.cowbel.ast.FunctionScopeConstructorNode;
import com.cowlark.cowbel.ast.IdentifierListNode;
import com.cowlark.cowbel.ast.IdentifierNode;
import com.cowlark.cowbel.ast.Node;
import com.cowlark.cowbel.ast.ParameterDeclarationListNode;
import com.cowlark.cowbel.ast.ParameterDeclarationNode;
import com.cowlark.cowbel.backend.Backend;
import com.cowlark.cowbel.errors.CompilationException;
import com.cowlark.cowbel.errors.FailedParseException;
import com.cowlark.cowbel.errors.FailedToInferTypeException;
import com.cowlark.cowbel.instructions.Instruction;
import com.cowlark.cowbel.instructions.InstructionVisitor;
import com.cowlark.cowbel.interfaces.HasMethods;
import com.cowlark.cowbel.interfaces.IsMethod;
import com.cowlark.cowbel.interfaces.IsMethodCallNode;
import com.cowlark.cowbel.interfaces.IsNode;
import com.cowlark.cowbel.parser.core.FailedParse;
import com.cowlark.cowbel.parser.core.Location;
import com.cowlark.cowbel.parser.core.MutableLocation;
import com.cowlark.cowbel.parser.core.ParseResult;
import com.cowlark.cowbel.parser.parsers.Parser;
import com.cowlark.cowbel.symbols.Variable;
import com.cowlark.cowbel.types.AbstractConcreteType;
import com.cowlark.cowbel.types.ExternObjectConcreteType;
import com.cowlark.cowbel.types.InterfaceConcreteType;
import com.cowlark.cowbel.types.ObjectConcreteType;

public class Compiler
{
	public static Compiler Instance;
	
	private CompilerListener _listener;
	private Backend _backend;
	private Location _input;
	private InterfaceContext _rootTypeContext;
	private Function _mainFunction;
	private TreeSet<IsNode> _methodCallNodes = new TreeSet<IsNode>();
	private FunctionScopeConstructorNode _ast;

	private ASTVisitor _record_variable_declarations_visitor =
		new RecordVariableDeclarationsVisitor();
	private ASTVisitor _resolve_variable_references_visitor =
		new ResolveVariableReferencesVisitor();
	private ASTVisitor _assign_functions_to_scopes_visitor =
		new AssignFunctionsToScopesVisitor();
	private ASTVisitor _assign_variables_to_constructors_visitor =
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
		
		_rootTypeContext = new InterfaceContext(_ast, null);
		
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
			
			FunctionSignature signature = new FunctionSignature(mainnode.getFunctionHeader());
			FunctionTemplate template = new FunctionTemplate(_rootTypeContext, mainnode);
			_mainFunction = template.instantiate(mainnode, signature, null);
			
			Function function;
			while (FunctionTemplate.pendingFunctions() ||
				!CollectTypeConstraintsVisitor.Instance.getUnhandledNodes().isEmpty())
			{
				function = FunctionTemplate.getNextPendingFunction();
				
				while (function != null)
				{
					record_type_declarations(function);
					record_variable_declarations(function);

					function.getBody().visit(CollectTypeConstraintsVisitor.Instance);
					
					function = FunctionTemplate.getNextPendingFunction();
				}
				
				/* will instantiate new functions */
				
				while (wire_methods_pass1())
					;
								
				if (!FunctionTemplate.pendingFunctions() &&
					!CollectTypeConstraintsVisitor.Instance.getUnhandledNodes().isEmpty())
				{
					/* There are unhandled nodes, but we can't handle any of them ---
					 * so throw an error. */
					
					IsMethod m = (IsMethod) CollectTypeConstraintsVisitor.Instance.getUnhandledNodes().first();
					throw new FailedToInferTypeException(m.getReceiver(), null);
				}
				
				/* We've processed all pending function instantiations. Now
				 * instantiate any functions that are used as methods. */
				
				for (Interface inter : Interface.getAllInterfaces())
					inter.instantiateMethods();
			}
		}

		wire_methods_pass2();
		_listener.onSymbolTableAnalysisEnd();
		
		/* Perform type checking and inference. */
		
		_listener.onTypeInferenceBegin();
		TypeInferenceEngine.undoCycles();
		TypeInferenceEngine.propagateConstraints();
		TypeInferenceEngine.assignConcreteTypes();
		visit(AssignCallablesToMethods.Instance);
		_listener.onTypeInferenceEnd();
		
		/* Construct basic blocks and IR representation. */
		
		_listener.onBasicBlockAnalysisBegin();
		
		visit(ClassifyScopeKindsVisitor.Instance);
		visit(AssignConstructorsToScopesVisitor.Instance);
		visit(AssignVariablesToConstructorsVisitor.Instance);
		
		for (Function f : Function.getAllFunctions())
			f.buildBasicBlocks();
		
		_listener.onBasicBlockAnalysisEnd();
		
		/* Code generation. */
		
		_listener.onCodeGenerationBegin();
		_backend.setMainFunction(_mainFunction);
		_backend.prologue();
		
		for (InterfaceConcreteType ctype : InterfaceConcreteType.getAllInterfaceTypes())
			_backend.visit(ctype);
		for (ObjectConcreteType ctype : ObjectConcreteType.getAllObjectTypes())
			_backend.visit(ctype);
		for (ExternObjectConcreteType ctype : ExternObjectConcreteType.getAllExternObjectTypes())
			_backend.visit(ctype);
		for (Constructor c : Constructor.getAllConstructors())
			_backend.visit(c);
		
		for (Function f : Function.getAllFunctions())
			_backend.compileFunction(f);
		
		_backend.epilogue();
		_listener.onCodeGenerationEnd();
	}

	public void visit(BasicBlockVisitor visitor)
	{
		TreeSet<BasicBlock> pending = new TreeSet<BasicBlock>();
		TreeSet<BasicBlock> seen = new TreeSet<BasicBlock>();
		
		for (Function function : Function.getAllFunctions())
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
	
	public void visit(ASTVisitor visitor) throws CompilationException
	{
		for (Function f : Function.getAllFunctions())
			f.getBody().visit(visitor);
	}
	
	public void dumpAnnotatedAST()
	{
		ASTVisitor visitor = new ASTDumperVisitor();
		
		for (Function function : Function.getAllFunctions())
		{
			System.out.println();
			System.out.println(function.toString());
			
			try
			{
				function.getDefinition().visit(visitor);
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
		
		for (Function function : Function.getAllFunctions())
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
		for (Constructor c : Constructor.getAllConstructors())
		{
			System.out.println(c.toString());
			c.dumpDetails();
			System.out.println("");
		}
	}
	
	private void add_parameters(
			InterfaceContext typecontext,
			FunctionDefinitionNode node,
			ParameterDeclarationListNode pdln, boolean isOutputParameter)
			throws CompilationException
	{
		FunctionScopeConstructorNode body = node.getFunctionBody();

		for (IsNode n : pdln)
		{
			ParameterDeclarationNode pdn = (ParameterDeclarationNode) n;
			IdentifierNode variablename = pdn.getIdentifier();
			
			TypeRef ptyperef = pdn.getTypeRef();
			TypeRef vartyperef = new TypeRef(pdn);
			if (isOutputParameter)
				ptyperef.addParent(vartyperef);
			else
				ptyperef.addChild(vartyperef);
			
			Variable v = new Variable(pdn, variablename, vartyperef);
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
		FunctionDefinitionNode node = (FunctionDefinitionNode) function.getDefinition();
		FunctionScopeConstructorNode body = node.getFunctionBody();
		
		body.visit(RecordTypeDefinitionsVisitor.Instance);
	}
	
	private void record_variable_declarations(Function function)
			throws CompilationException
	{
		InterfaceContext typecontext = function.getTypeContext();
		FunctionDefinitionNode node = (FunctionDefinitionNode) function.getDefinition();
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
		
		body.visit(AssignFunctionsToScopesVisitor.Instance);
		body.visit(RecordVariableDeclarationsVisitor.Instance);
		body.visit(ResolveVariableReferencesVisitor.Instance);
	}
	
	@SuppressWarnings("unchecked")
    private <T extends Node & IsMethod> T castNode(Node node)
	{
		return (T) node;
	}
	
	private boolean wire_methods_pass1() throws CompilationException
	{
		TreeSet<IsNode> unhandledNodes = CollectTypeConstraintsVisitor.Instance.getUnhandledNodes();

		if (!unhandledNodes.isEmpty())
		{
			/* Find the first unhandled node which has concrete enough types for
			 * us to process it, and do so. */
			
			for (IsNode n : unhandledNodes)
			{
				IsMethodCallNode methodcall = (IsMethodCallNode) n;
				AbstractExpressionNode receiver = methodcall.getReceiver();
				TypeRef tr = receiver.getTypeRef();
				Set<HasMethods> is = tr.collectMethodProviders();
				if (is != null)
				{
					/* Look for anything willing to implement this method. */
					
					FunctionTemplateSignature fts = new FunctionTemplateSignature(methodcall);
					TreeSet<Callable> methods = new TreeSet<Callable>();
					for (HasMethods hm : is)
					{
						Callable c = hm.lookupMethod(fts, methodcall);
						if (c != null)
							methods.add(c);
					}
					
					if (!methods.isEmpty())
					{
						for (Callable c : methods)
						{
							c.wireTypesToCall(methodcall);
							CollectTypeConstraintsVisitor.Instance.unhandledNodeIsHandled(methodcall);
							_methodCallNodes.add(methodcall);
						}
						return true;
					}
				}
			}			
		}
		
		return false;
	}

	private void wire_methods_pass2()
			throws CompilationException
	{
		/* Now we need to wire each interface's header to the implementations
		 * that are actually doing the work. */
		
		for (Interface interf : Interface.getAllInterfaces())
			interf.instantiateMethods();
	}
	
	public void dumpTypeRefGraph(PrintWriter pw) throws IOException
	{
		pw.println("digraph {");
		for (TypeRef typeref : TypeRef.getAllTypeRefs())
		{
			IsNode node = typeref.getNode();
			pw.println(typeref.toString() + " [");
			pw.print("label=\"");
			pw.print("" + typeref.getConcreteType() + "\\n");
			pw.print(node.getClass().getSimpleName() + " " + typeref.getId() + "\\n");
			pw.print(node.getNode().locationAsString() + "\\n");
			
			for (Interface constraint : typeref.getConstraints())
				pw.print("" + constraint.getNameHint() + "\\n");
			
			pw.println("\"");
			pw.println("];");
			
			for (TypeRef child : typeref.getChildren())
			{
				pw.println(typeref.toString() + " -> " + child.toString() + ";");
			}
		}
		pw.println("}");
	}
	
	public Interface getPrimitiveInterface(String name)
	{
		return _rootTypeContext.lookupRawType(name);
	}
	
	public TypeRef getCanonicalPrimitiveTypeRef(String name)
	{
		try
		{
			IdentifierNode id = IdentifierNode.createIdentifier(name);
			return _mainFunction.getScope().lookupVariable(id).getTypeRef();
		}
		catch (CompilationException e)
		{
			throw new UnsupportedOperationException(e);
		}
	}
	
	public void dumpConcreteTypes()
	{
		for (AbstractConcreteType ctype : AbstractConcreteType.getAllConcreteTypes())
		{
			System.out.println("");
			ctype.dump(System.out);
		}
	}
}
