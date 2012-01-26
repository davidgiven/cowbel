package com.cowlark.cowbel;

import java.util.Collections;
import java.util.Set;
import java.util.TreeSet;
import com.cowlark.cowbel.ast.nodes.FunctionDefinitionNode;
import com.cowlark.cowbel.ast.nodes.FunctionHeaderNode;
import com.cowlark.cowbel.ast.nodes.FunctionScopeConstructorNode;
import com.cowlark.cowbel.ast.nodes.IdentifierNode;
import com.cowlark.cowbel.ast.nodes.ParameterDeclarationListNode;
import com.cowlark.cowbel.ast.nodes.ScopeConstructorNode;
import com.cowlark.cowbel.ast.nodes.StatementNode;
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
import com.cowlark.cowbel.symbols.Function;

public class Compiler
{
	private CompilerListener _listener;
	private Backend _backend;
	private Location _input;
	private Function _mainFunction;
	private TreeSet<Function> _functions;
	private TreeSet<Constructor> _constructors;
	private FunctionScopeConstructorNode _ast;
	
	public Compiler()
    {
		_listener = new CompilerListenerAdapter();
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
	
	public ScopeConstructorNode getAst()
	{
		return (ScopeConstructorNode) _ast;
	}
	
	public Set<Function> getFunctions()
	{
	    return Collections.unmodifiableSet(_functions);
    }
	
	public Set<Constructor> getConstructors()
	{
	    return Collections.unmodifiableSet(_constructors);
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
		
		/* Analyse symbol tables. */
		
		_listener.onSymbolTableAnalysisBegin();
		{
			Location loc = new Location("", "<internal>");
			Location mainname = new Location("<main>", "<internal>");
			MutableLocation mainnameend = new MutableLocation(mainname);
			mainnameend.advance(6);
			
			FunctionHeaderNode toplevelnodeheader = new FunctionHeaderNode(
					loc, loc,
					new IdentifierNode(mainname, mainnameend),
					new ParameterDeclarationListNode(loc, loc),
					new ParameterDeclarationListNode(loc, loc));
			FunctionDefinitionNode toplevelnode = new FunctionDefinitionNode(
					loc, loc, toplevelnodeheader, _ast);
			_mainFunction = new Function(toplevelnode);
			_ast.setFunction(_mainFunction);

			_ast.visit(new RecordVariableDeclarationsVisitor(_ast));
			_ast.visit(new ResolveVariableReferencesVisitor());
			
			_functions = new TreeSet<Function>();
			_ast.visit(new AssignFunctionsToScopesVisitor(_functions));
			
			_constructors = new TreeSet<Constructor>();
			_ast.visit(new AssignConstructorsToScopesVisitor(_constructors));
			
			_ast.visit(new AssignVariablesToConstructorsVisitor());
	
			_ast.addSymbol(_mainFunction);
			toplevelnode.setSymbol(_mainFunction);
		}
		_listener.onSymbolTableAnalysisEnd();
		
		/* Type check everything. */
		
		_listener.onTypeCheckBegin();
		{
			_ast.checkTypes();
			for (Function f : _functions)
			{
				FunctionDefinitionNode node = (FunctionDefinitionNode) f.getNode();
				StatementNode body = node.getFunctionBody();
				if (body != _ast)
					body.checkTypes();
			}
		}
		_listener.onTypeCheckEnd();
		
//		/* Construct basic blocks and IR representation. */
//		
//		_listener.onBasicBlockAnalysisBegin();
//		
//		for (Function f : _functions)
//			f.buildBasicBlocks();
//		
//		_listener.onBasicBlockAnalysisEnd();
//		
//		/* Code generation. */
//		
//		_listener.onCodeGenerationBegin();
//		_backend.prologue();
//		for (Constructor c : _constructors)
//			_backend.visit(c);
//		for (Function f : _functions)
//			_backend.compileFunction(f);
//		_backend.epilogue();
//		_listener.onCodeGenerationEnd();
	}

	public void visit(BasicBlockVisitor visitor)
	{
		TreeSet<BasicBlock> pending = new TreeSet<BasicBlock>();
		TreeSet<BasicBlock> seen = new TreeSet<BasicBlock>();
		
		for (Function f : _functions)
		{
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
	
	public void dumpBasicBlocks()
	{
		TreeSet<BasicBlock> pending = new TreeSet<BasicBlock>();
		TreeSet<BasicBlock> seen = new TreeSet<BasicBlock>();
		
		for (Function f : _functions)
		{
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
}
