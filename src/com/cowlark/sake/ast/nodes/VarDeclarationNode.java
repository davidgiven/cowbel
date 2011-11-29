package com.cowlark.sake.ast.nodes;

import com.cowlark.sake.parser.core.Location;

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
