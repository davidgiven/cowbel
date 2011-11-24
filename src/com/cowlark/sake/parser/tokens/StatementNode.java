package com.cowlark.sake.parser.tokens;

import com.cowlark.sake.parser.core.Location;

public class StatementNode extends Node
{
	public StatementNode(Location start, Location end)
    {
        super(start, end);
    }
}