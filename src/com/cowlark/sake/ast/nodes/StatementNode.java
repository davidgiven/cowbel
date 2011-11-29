package com.cowlark.sake.ast.nodes;

import com.cowlark.sake.parser.core.Location;

public class StatementNode extends Node
{
	public StatementNode(Location start, Location end)
    {
        super(start, end);
    }
}