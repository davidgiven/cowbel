package com.cowlark.sake.parser.tokens;

import com.cowlark.sake.parser.core.Location;

public class ParameterDeclarationNode extends ExpressionNode
{
	public ParameterDeclarationNode(Location start, Location end,
			IdentifierNode name, TypeNode type)
    {
		super(start, end);
		addChild(name);
		addChild(type);
    }
	
	@Override
	public String getShortDescription()
	{
	    return getText();
	}
}
