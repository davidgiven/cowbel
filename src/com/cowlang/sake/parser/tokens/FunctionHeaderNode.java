package com.cowlang.sake.parser.tokens;

import com.cowlang.sake.parser.core.Location;

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
