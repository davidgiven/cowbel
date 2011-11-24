package com.cowlark.sake.parser.tokens;

import com.cowlark.sake.parser.core.Location;

public class VarAssignmentNode extends StatementNode
{
	public VarAssignmentNode(Location start, Location end,
			IdentifierNode identifier, ExpressionNode value)
    {
		super(start, end);
		addChild(identifier);
		addChild(value);
    }
	
	@Override
	public String getShortDescription()
	{
	    return getText();
	}
}
