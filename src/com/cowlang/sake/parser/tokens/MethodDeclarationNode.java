package com.cowlang.sake.parser.tokens;

import com.cowlang.sake.parser.core.Location;

public class MethodDeclarationNode extends ExpressionNode
{
	public MethodDeclarationNode(Location start, Location end,
    		MethodHeaderNode header)
    {
        super(start, end);
        addChild(header);
    }
    	
}
