package com.cowlark.sake.parser.tokens;

import com.cowlark.sake.parser.core.Location;
import com.cowlark.sake.parser.core.Token;

public class SyntacticElementToken extends Token
{
	public SyntacticElementToken(Location start, Location end)
    {
        super(start, end);
    }
}
