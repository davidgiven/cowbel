package com.cowlang2.parser.tokens;

import com.cowlang2.parser.core.Location;

public class EOFToken extends TextToken
{
	public EOFToken(Location location)
    {
	    super(location, location);
    }
}
