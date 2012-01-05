package com.cowlark.sake.instructions;

public class InstructionVisitor
{
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
