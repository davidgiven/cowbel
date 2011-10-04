package com.cowlang2.parser.tokens;

import com.cowlang2.parser.core.Location;

public class MethodDeclarationNode extends ExpressionNode
{
	public MethodDeclarationNode(Location start, Location end,
			IdentifierNode name, TypexDeclarationNode typeparams,
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
