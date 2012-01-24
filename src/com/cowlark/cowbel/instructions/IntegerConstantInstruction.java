package com.cowlark.cowbel.instructions;

import com.cowlark.cowbel.ast.nodes.Node;

public class IntegerConstantInstruction extends Instruction
{
	private long _value;
	
	public IntegerConstantInstruction(Node node, long value)
    {
		super(node, 0);
		_value = value;
    }
	
	public long getValue()
	{
		return _value;
	}
	
	@Override
	protected String getInstructionName()
	{
	    return "IntegerConstant";
	}
	
	@Override
	protected String getShortDescription()
	{
	    return Long.toString(_value);
	}
	
	public void visit(InstructionVisitor visitor)
	{
		visitor.visit(this);
	}
}
