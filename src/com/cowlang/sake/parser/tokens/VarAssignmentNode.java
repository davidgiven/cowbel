package com.cowlang.sake.parser.tokens;

import com.cowlang.sake.parser.core.Location;

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
