package com.cowlang.sake.parser.tokens;

import com.cowlang.sake.parser.core.Location;

public class IdentifierNode extends ExpressionNode
{
	public IdentifierNode(Location start, Location end)
    {
		super(start, end);
    }
	
	@Override
	public String getShortDescription()
	{
	    return getText();
	}
}
