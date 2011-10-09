package com.cowlang2.parser.tokens;

import com.cowlang2.parser.core.Location;

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