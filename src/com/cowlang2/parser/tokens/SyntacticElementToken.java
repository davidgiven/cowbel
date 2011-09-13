package com.cowlang2.parser.tokens;

import com.cowlang2.parser.core.Location;
import com.cowlang2.parser.core.Token;

public class SyntacticElementToken extends Token
{
	public SyntacticElementToken(Location start, Location end)
    {
        super(start, end);
    }
}
