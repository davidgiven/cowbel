package com.cowlang.sake.parser.tokens;

import com.cowlang.sake.parser.core.Location;

public class StringConstantNode extends ExpressionNode
{
	private String _value;
	
	public StringConstantNode(Location start, Location end, String value)
    {
        super(start, end);
        _value = value;
    }
}
