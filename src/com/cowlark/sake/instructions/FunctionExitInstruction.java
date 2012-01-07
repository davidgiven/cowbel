package com.cowlark.sake.instructions;

import com.cowlark.sake.ast.nodes.Node;

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
