package com.cowlang2.parser.core;

public class Token extends ParseResult
{
	public Token(Location start, Location end)
    {
		super(start, end);
    }
	
	@Override
	public boolean failed()
	{
	    return false;
	}
}
