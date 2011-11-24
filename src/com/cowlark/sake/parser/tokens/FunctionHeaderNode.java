package com.cowlark.sake.parser.tokens;

import com.cowlark.sake.parser.core.Location;

public class FunctionHeaderNode extends Node
{
	public FunctionHeaderNode(Location start, Location end,
			IdentifierNode name,
			ParameterDeclarationListNode inputparams,
			TypeNode returntype)
    {
		super(start, end);
		addChild(name);
		addChild(inputparams);
		addChild(returntype);
    }	
}
