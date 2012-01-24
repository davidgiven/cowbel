package com.cowlark.cowbel.ast.nodes;

import com.cowlark.cowbel.parser.core.Location;
import com.cowlark.cowbel.parser.core.Token;

public class SyntacticElementToken extends Token
{
	public SyntacticElementToken(Location start, Location end)
    {
        super(start, end);
    }
}
