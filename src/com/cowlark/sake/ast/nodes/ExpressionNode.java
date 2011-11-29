package com.cowlark.sake.ast.nodes;

import com.cowlark.sake.parser.core.Location;

public class ExpressionNode extends Node
{
	public ExpressionNode(Location start, Location end)
    {
        super(start, end);
    }
}