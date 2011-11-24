package com.cowlang.sake.parser.tokens;

import com.cowlang.sake.parser.core.Location;

public class StringTypeNode extends Node
{
	public StringTypeNode(Location start, Location end)
    {
        super(start, end);
    }
}