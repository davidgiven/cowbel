package com.cowlang.sake.parser.tokens;

import com.cowlang.sake.parser.core.Location;

public class VarDeclarationNode extends StatementNode
{
	public VarDeclarationNode(Location start, Location end,
			IdentifierNode identifier)
    {
		super(start, end);
		addChild(identifier);
    }
	
	@Override
	public String getShortDescription()
	{
	    return getText();
	}
}
