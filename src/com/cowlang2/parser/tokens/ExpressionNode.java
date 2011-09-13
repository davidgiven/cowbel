package com.cowlang2.parser.tokens;

import com.cowlang2.parser.core.Location;

public class ExpressionNode extends Node
{
	public ExpressionNode(Location start, Location end)
    {
        super(start, end);
    }
}