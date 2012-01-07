package com.cowlark.sake.backend;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.HashSet;
import java.util.Iterator;
import org.apache.commons.io.IOUtils;
import com.cowlark.sake.BasicBlock;
import com.cowlark.sake.Function;
import com.cowlark.sake.instructions.Instruction;
import com.cowlark.sake.instructions.InstructionVisitor;

public abstract class Backend extends InstructionVisitor
{
	private HashSet<BasicBlock> _pending = new HashSet<BasicBlock>();
	private HashSet<BasicBlock> _seen = new HashSet<BasicBlock>();
	
	private Iterator<Instruction> _iterator;
	private PrintStream _stream;
	private BasicBlock _currentbb;
	private Throwable _error;
	
	public Backend(OutputStream stream)
    {
		_stream = new PrintStream(stream);
    }
	
	protected BasicBlock getCurrentBasicBlock()
	{
		return _currentbb;
	}
	
	protected void setError(Throwable t)
	{
		if (_error == null)
			_error = t;
	}
	
	protected void print(InputStream is)
	{
		try
		{
			IOUtils.copy(is, _stream);
		}
		catch (IOException e)
		{
			setError(e);
		}
	}
	
	protected <T> void print(T value)
	{
		_stream.print(value);
	}
	
	public void prologue()
	{
	}
	
	public void epilogue()
	{
	}
	
	public void compileFunction(Function f)
	{
		_pending.add(f.getEntryBB());
		_seen.add(f.getEntryBB());
		
		while (!_pending.isEmpty())
		{
			BasicBlock bb = _pending.iterator().next();
			_pending.remove(bb);
	
			for (BasicBlock b : bb.getDestinationBlocks())
			{
				if (!_seen.contains(b))
				{
					_seen.add(b);
					_pending.add(b);
				}
			}
			
			compileBasicBlock(bb);
		}
	}
	
	public void compileBasicBlock(BasicBlock bb)
	{
		_currentbb = bb;
		_iterator = bb.getInstructions().iterator();
		while (_iterator.hasNext())
			compileFromIterator();
	}
	
	protected void compileFromIterator()
	{
		Instruction insn = _iterator.next();
		insn.visit(this);
	}
	
	@Override
	public void visit(Instruction insn)
	{
		assert(false);
	}
}
