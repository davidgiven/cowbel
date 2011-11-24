package com.cowlark.sake.parser.tokens;

import com.cowlark.sake.parser.core.Location;

public class VariableReadNode extends ExpressionNode
{
	public VariableReadNode(Location start, Location end)
    {
		super(start, end);
    }
	
	@Override
	public String getShortDescription()
	{
	    return getText();
	}
}
