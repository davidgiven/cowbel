package com.cowlark.sake.instructions;

import com.cowlark.sake.ast.nodes.Node;

public class ListConstructorInstruction extends Instruction
{
	public ListConstructorInstruction(Node node, int length)
    {
		super(node, length);
    }
	
	@Override
	protected String getInstructionName()
	{
	    return "ListConstructor";
	}
}
