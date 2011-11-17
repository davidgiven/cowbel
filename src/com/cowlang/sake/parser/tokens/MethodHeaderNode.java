package com.cowlang.sake.parser.tokens;

import com.cowlang.sake.parser.core.Location;

public class MethodHeaderNode extends Node
{
	public MethodHeaderNode(Location start, Location end,
			IdentifierNode name, TypeParameterDeclarationListNode typeparams,
			ParameterDeclarationListNode inputparams,
			ParameterDeclarationListNode outputparams)
    {
		super(start, end);
		addChild(name);
		addChild(typeparams);
		addChild(inputparams);
		addChild(outputparams);
    }	
}
