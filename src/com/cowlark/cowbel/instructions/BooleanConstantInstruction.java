package com.cowlark.cowbel.instructions;

import com.cowlark.cowbel.ast.nodes.Node;

public class BooleanConstantInstruction extends Instruction
{
	private boolean _value;
	
	public BooleanConstantInstruction(Node node, boolean value)
    {
		super(node, 0);
		_value = value;
    }
	
	public boolean getValue()
	{
		return _value;
	}
	
	@Override
	protected String getInstructionName()
	{
	    return "BooleanConstant";
	}
	
	@Override
	protected String getShortDescription()
	{
	    return Boolean.toString(_value);
	}
	
	public void visit(InstructionVisitor visitor)
	{
		visitor.visit(this);
	}
}
