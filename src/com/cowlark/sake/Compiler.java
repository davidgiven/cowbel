package com.cowlark.sake;

import java.util.TreeSet;
import com.cowlark.sake.ast.nodes.FunctionDefinitionNode;
import com.cowlark.sake.ast.nodes.FunctionHeaderNode;
import com.cowlark.sake.ast.nodes.IdentifierNode;
import com.cowlark.sake.ast.nodes.ParameterDeclarationListNode;
import com.cowlark.sake.ast.nodes.ScopeNode;
import com.cowlark.sake.ast.nodes.StatementNode;
import com.cowlark.sake.ast.nodes.VoidTypeNode;
import com.cowlark.sake.backend.Backend;
import com.cowlark.sake.errors.CompilationException;
import com.cowlark.sake.errors.FailedParseException;
import com.cowlark.sake.instructions.Instruction;
import com.cowlark.sake.instructions.InstructionVisitor;
import com.cowlark.sake.parser.core.FailedParse;
import com.cowlark.sake.parser.core.Location;
import com.cowlark.sake.parser.core.MutableLocation;
import com.cowlark.sake.parser.core.ParseResult;
import com.cowlark.sake.parser.parsers.Parser;
import com.cowlark.sake.symbols.Function;
import com.cowlark.sake.symbols.GlobalSymbolStorage;

public class Compiler
{
	private CompilerListener _listener;
	private Backend _backend;
	private Location _input;
	private ParseResult _ast;
	private GlobalSymbolStorage _globals;
	private Function _mainFunction;
	
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
	
	public ScopeNode getAst()
	{
		return (ScopeNode) _ast;
	}
	
	public void compile() throws CompilationException
	{
		_listener.onParseBegin();
		_ast = Parser.ProgramParser.parse(_input);
		_listener.onParseEnd();
		
		if (_ast.failed())
		{
			FailedParse fp = (FailedParse) _ast;
			throw new FailedParseException(fp);
		}

		ScopeNode ast = getAst();
		
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
					new VoidTypeNode(loc, loc));
			FunctionDefinitionNode toplevelnode = new FunctionDefinitionNode(
					loc, loc, toplevelnodeheader, ast);
			_mainFunction = new Function(toplevelnode);

			/* Override the root scope's storage. */
			_globals = new GlobalSymbolStorage();
			ast.setSymbolStorage(_globals);
			
			ast.visit(new RecordVariableDeclarationsVisitor(ast));
			ast.visit(new ResolveVariableReferencesVisitor());
	
			_globals.addSymbol(_mainFunction);
			toplevelnode.setSymbol(_mainFunction);
		}
		_listener.onSymbolTableAnalysisEnd();
		
		/* Type check everything. */
		
		_listener.onTypeCheckBegin();
		{
			ast.checkTypes();
			for (Function f : _globals.getFunctions())
			{
				FunctionDefinitionNode node = (FunctionDefinitionNode) f.getNode();
				StatementNode body = node.getFunctionBody();
				if (body != ast)
					body.checkTypes();
			}
		}
		_listener.onTypeCheckEnd();
		
		/* Construct basic blocks and IR representation. */
		
		_listener.onBasicBlockAnalysisBegin();
		
		for (Function f : _globals.getFunctions())
			f.buildBasicBlocks();
		
		_listener.onBasicBlockAnalysisEnd();
		
		/* Dataflow analysis. */
		
		_listener.onDataflowAnalysisBegin();
		{
			for (Function f : _globals.getFunctions())
				DataflowAnalyser.initialiseFunction(f);
			
			DataflowAnalyser da = new DataflowAnalyser();
			do
			{
				da.reset();
				visit(da);
			}
			while (!da.isFinished());
		}
		_listener.onDataflowAnalysisEnd();
		
		/* Code generation. */
		
		_listener.onCodeGenerationBegin();
		for (Function f : _globals.getFunctions())
			_backend.compileFunction(f);
		_listener.onCodeGenerationEnd();
	}

	public void visit(BasicBlockVisitor visitor)
	{
		TreeSet<BasicBlock> pending = new TreeSet<BasicBlock>();
		TreeSet<BasicBlock> seen = new TreeSet<BasicBlock>();
		
		for (Function f : _globals.getFunctions())
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
		
		for (Function f : _globals.getFunctions())
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
}
