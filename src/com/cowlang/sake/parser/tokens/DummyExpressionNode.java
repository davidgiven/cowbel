package com.cowlang.sake.parser.tokens;

import com.cowlang.sake.parser.core.Location;

public class DummyExpressionNode extends ExpressionNode
{
	public DummyExpressionNode(Location start, Location end, ExpressionNode child)
    {
        super(start, end);
        addChild(child);
    }
}