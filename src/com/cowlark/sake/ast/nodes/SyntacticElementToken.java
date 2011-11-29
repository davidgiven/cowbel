package com.cowlark.sake.ast.nodes;

import com.cowlark.sake.parser.core.Location;
import com.cowlark.sake.parser.core.Token;

public class SyntacticElementToken extends Token
{
	public SyntacticElementToken(Location start, Location end)
    {
        super(start, end);
    }
}
