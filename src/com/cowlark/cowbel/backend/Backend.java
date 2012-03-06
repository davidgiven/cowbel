/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING.BSD in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel.backend;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Iterator;
import java.util.TreeSet;
import org.apache.commons.io.IOUtils;
import com.cowlark.cowbel.core.BasicBlock;
import com.cowlark.cowbel.core.Compiler;
import com.cowlark.cowbel.core.Constructor;
import com.cowlark.cowbel.core.Function;
import com.cowlark.cowbel.errors.CompilationException;
import com.cowlark.cowbel.instructions.Instruction;
import com.cowlark.cowbel.instructions.InstructionVisitor;
import com.cowlark.cowbel.types.ExternObjectConcreteType;
import com.cowlark.cowbel.types.InterfaceConcreteType;
import com.cowlark.cowbel.types.ObjectConcreteType;

public abstract class Backend extends InstructionVisitor
{
	private TreeSet<BasicBlock> _pending = new TreeSet<BasicBlock>();
	private TreeSet<BasicBlock> _seen = new TreeSet<BasicBlock>();
	
	private Iterator<Instruction> _iterator;
	private Compiler _compiler;
	private PrintStream _stream;
	private BasicBlock _currentbb;
	private Throwable _error;
	
	public Backend(Compiler compiler, OutputStream stream)
    {
		_compiler = compiler;
		_stream = new PrintStream(stream);
    }
	
	public abstract void setMainFunction(Function mainFunction);
	
	protected Compiler getCompiler()
	{
		return _compiler;
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
	
	protected void print(char c)
	{
		_stream.print(c);
		_stream.flush();
	}
	
	protected <T> void print(T value)
	{
		_stream.print(value);
		_stream.flush();
	}
	
	public void prologue() throws CompilationException
	{
	}
	
	public void epilogue() throws CompilationException
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
	
	public abstract void visit(InterfaceConcreteType itype);
	public abstract void visit(ObjectConcreteType itype);
	public abstract void visit(ExternObjectConcreteType itype);
	public abstract void visit(Constructor constructor);
	
	@Override
	public void visit(Instruction insn)
	{
		assert(false);
	}
}
