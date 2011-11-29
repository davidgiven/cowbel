package com.cowlark.sake.ast.nodes;

import com.cowlark.sake.parser.core.Location;

public class StringTypeNode extends Node
{
	public StringTypeNode(Location start, Location end)
    {
        super(start, end);
    }
}