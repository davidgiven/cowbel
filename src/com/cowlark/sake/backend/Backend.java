package com.cowlark.sake.backend;

import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Iterator;
import com.cowlark.sake.BasicBlock;
import com.cowlark.sake.instructions.Instruction;
import com.cowlark.sake.instructions.InstructionVisitor;

public abstract class Backend extends InstructionVisitor
{
	private Iterator<Instruction> _iterator;
	private PrintStream _stream;
	
	public Backend(OutputStream stream)
    {
		_stream = new PrintStream(stream);
    }
	
	protected <T> void print(T value)
	{
		_stream.print(value);
	}
	
	public void compileBasicBlock(BasicBlock bb)
	{
		_iterator = bb.getInstructions().iterator();
		while (_iterator.hasNext())
			compileFromIterator();
	}
	
	private void compileFromIterator()
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
