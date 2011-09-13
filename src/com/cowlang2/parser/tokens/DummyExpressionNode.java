package com.cowlang2.parser.tokens;

import com.cowlang2.parser.core.Location;

public class DummyExpressionNode extends ExpressionNode
{
	public DummyExpressionNode(Location start, Location end, ExpressionNode child)
    {
        super(start, end);
        addChild(child);
    }
}