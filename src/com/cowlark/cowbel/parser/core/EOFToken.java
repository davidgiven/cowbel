package com.cowlark.cowbel.parser.core;


public class EOFToken extends TextToken
{
	public EOFToken(Location location)
    {
	    super(location, location);
    }
}
