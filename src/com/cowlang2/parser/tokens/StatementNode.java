package com.cowlang2.parser.tokens;

import com.cowlang2.parser.core.Location;

public class StatementNode extends Node
{
	public StatementNode(Location start, Location end)
    {
        super(start, end);
    }
}