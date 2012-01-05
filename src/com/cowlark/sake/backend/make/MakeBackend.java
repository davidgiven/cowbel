package com.cowlark.sake.backend.make;

import java.io.OutputStream;
import com.cowlark.sake.BasicBlock;
import com.cowlark.sake.backend.Backend;
import com.cowlark.sake.instructions.IfInstruction;
import com.cowlark.sake.instructions.SetReturnValueInstruction;

public class MakeBackend extends Backend
{
	public MakeBackend(OutputStream stream)
    {
		super(stream);
    }
	
	@Override
	public void compileBasicBlock(BasicBlock bb)
	{
	    print(bb.toString());
	    print(" = ");
	    
	    super.compileBasicBlock(bb);
	    
	    print("\n");
	}
	
	@Override
	public void visit(IfInstruction insn)
	{
		assert(false);
	}
	
	@Override
	public void visit(SetReturnValueInstruction insn)
	{
		assert(false);
	}
}
