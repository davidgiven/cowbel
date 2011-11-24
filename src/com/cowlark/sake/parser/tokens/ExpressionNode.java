package com.cowlark.sake.parser.tokens;

import com.cowlark.sake.parser.core.Location;

public class ExpressionNode extends Node
{
	public ExpressionNode(Location start, Location end)
    {
        super(start, end);
    }
}