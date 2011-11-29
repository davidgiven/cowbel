package com.cowlark.sake.ast.nodes;

import com.cowlark.sake.parser.core.Location;

public class TypeNode extends Node
{
	public TypeNode(Location start, Location end)
    {
        super(start, end);
    }
}