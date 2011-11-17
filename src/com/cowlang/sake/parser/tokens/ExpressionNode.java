package com.cowlang.sake.parser.tokens;

import com.cowlang.sake.parser.core.Location;

public class ExpressionNode extends Node
{
	public ExpressionNode(Location start, Location end)
    {
        super(start, end);
    }
}