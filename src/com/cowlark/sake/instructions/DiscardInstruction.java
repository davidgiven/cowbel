package com.cowlark.sake.instructions;

import com.cowlark.sake.ast.nodes.Node;

public class DiscardInstruction extends Instruction
{
	public DiscardInstruction(Node node)
    {
		super(node, 1);
    }
	
	@Override
	protected String getInstructionName()
	{
	    return "Discard";
	}
	
	public void visit(InstructionVisitor visitor)
	{
		visitor.visit(this);
	}
}
