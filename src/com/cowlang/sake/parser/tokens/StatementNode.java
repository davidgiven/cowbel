package com.cowlang.sake.parser.tokens;

import com.cowlang.sake.parser.core.Location;

public class StatementNode extends Node
{
	public StatementNode(Location start, Location end)
    {
        super(start, end);
    }
}