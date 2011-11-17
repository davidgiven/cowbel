package com.cowlang.sake.parser.core;

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
	
	@Override
	public String toString()
	{
		return getClass().toString() + "='" + getText() + "'";
	}
}
