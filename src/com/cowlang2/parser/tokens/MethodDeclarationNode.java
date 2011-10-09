package com.cowlang2.parser.tokens;

import com.cowlang2.parser.core.Location;

public class MethodDeclarationNode extends ExpressionNode
{
	public MethodDeclarationNode(Location start, Location end,
    		MethodHeaderNode header)
    {
        super(start, end);
        addChild(header);
    }
    	
}
