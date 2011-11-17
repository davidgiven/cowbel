package com.cowlang.sake.parser.tokens;

import com.cowlang.sake.parser.core.Location;

public class ParameterDeclarationNode extends ExpressionNode
{
	public ParameterDeclarationNode(Location start, Location end,
			IdentifierNode name, TypeReferenceNode type)
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
