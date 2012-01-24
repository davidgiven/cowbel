package com.cowlark.cowbel.instructions;

import com.cowlark.cowbel.ast.nodes.Node;

public class FunctionExitInstruction extends Instruction
{
	public FunctionExitInstruction(Node node)
    {
		super(node, 0);
    }
	
	@Override
	protected String getInstructionName()
	{
		return "FunctionExit";
	}
	
	public void visit(InstructionVisitor visitor)
	{
		visitor.visit(this);
	}
}
