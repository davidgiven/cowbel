package com.cowlang.sake.parser.tokens;

import com.cowlang.sake.parser.core.Location;

public class MethodDefinitionNode extends ObjectInstantiationMemberNode
{
	public MethodDefinitionNode(Location start, Location end,
			MethodHeaderNode header, BlockNode body)
    {
		super(start, end);
		addChild(header);
		addChild(body);
    }	
}