package com.cowlark.sake.instructions;

import com.cowlark.sake.ast.nodes.Node;

public class StringConstantInstruction extends Instruction
{
	private String _value;
	
	public StringConstantInstruction(Node node, String value)
    {
		super(node, 0);
		_value = value;
    }
	
	public String getValue()
	{
		return _value;
	}
	
	@Override
	protected String getInstructionName()
	{
	    return "StringConstant";
	}
	
	@Override
	protected String getShortDescription()
	{
	    return "<" + _value + ">";
	}
	
	public void visit(InstructionVisitor visitor)
	{
		visitor.visit(this);
	}
}
