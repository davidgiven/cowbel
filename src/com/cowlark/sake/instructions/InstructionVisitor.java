package com.cowlark.sake.instructions;

import com.cowlark.sake.BasicBlock;

public class InstructionVisitor
{
	public void visit(BasicBlock bb)
	{
	}
	
	public void visit(Instruction insn)
	{
	}
	
	public void visit(IfInstruction insn)
	{
		visit((Instruction) insn);
	}
	
	public void visit(SetReturnValueInstruction insn)
	{
		visit((Instruction) insn);
	}
	
	public void visit(GetLocalVariableInstruction insn)
	{
		visit((Instruction) insn);
	}
	
	public void visit(SetLocalVariableInInstruction insn)
	{
		visit((Instruction) insn);
	}	
}
