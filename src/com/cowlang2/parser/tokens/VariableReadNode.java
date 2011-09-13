package com.cowlang2.parser.tokens;

import com.cowlang2.parser.core.Location;

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
