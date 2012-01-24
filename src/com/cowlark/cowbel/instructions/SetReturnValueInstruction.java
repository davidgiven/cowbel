package com.cowlark.cowbel.instructions;

import com.cowlark.cowbel.ast.nodes.Node;

public class SetReturnValueInstruction extends Instruction
{
	public SetReturnValueInstruction(Node node)
    {
		super(node, 1);
    }
	
	@Override
	protected String getInstructionName()
	{
		return "SetReturnValue";
	}
	
	public void visit(InstructionVisitor visitor)
	{
		visitor.visit(this);
	}
}
