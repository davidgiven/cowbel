package com.cowlark.cowbel.instructions;

import com.cowlark.cowbel.ast.nodes.Node;

public class ArrayConstructorInstruction extends Instruction
{
	public ArrayConstructorInstruction(Node node, int length)
    {
		super(node, length);
    }
	
	@Override
	protected String getInstructionName()
	{
	    return "ArrayConstructor";
	}
	
	public void visit(InstructionVisitor visitor)
	{
		visitor.visit(this);
	}
}
