package com.cowlark.sake.ast.nodes;

import com.cowlark.sake.parser.core.Location;

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
