package com.cowlang.sake.parser.tokens;

import com.cowlang.sake.parser.core.Location;

public class FunctionDefinitionNode extends StatementNode
{
	public FunctionDefinitionNode(Location start, Location end,
			FunctionHeaderNode header, BlockNode body)
    {
		super(start, end);
		addChild(header);
		addChild(body);
    }	
}