package com.cowlang.sake.parser.tokens;

import com.cowlang.sake.parser.core.Location;
import com.cowlang.sake.parser.core.Token;

public class SyntacticElementToken extends Token
{
	public SyntacticElementToken(Location start, Location end)
    {
        super(start, end);
    }
}
