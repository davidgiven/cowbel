package com.cowlark.sake.instructions;

import com.cowlark.sake.ast.nodes.Node;

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
}
