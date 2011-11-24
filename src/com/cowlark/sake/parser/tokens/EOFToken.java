package com.cowlark.sake.parser.tokens;

import com.cowlark.sake.parser.core.Location;

public class EOFToken extends TextToken
{
	public EOFToken(Location location)
    {
	    super(location, location);
    }
}
