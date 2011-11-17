package com.cowlang.sake.parser.tokens;

import com.cowlang.sake.parser.core.Location;

public class EOFToken extends TextToken
{
	public EOFToken(Location location)
    {
	    super(location, location);
    }
}
