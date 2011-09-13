package com.cowlang2.parser.core;

public class FailedParse extends ParseResult
{
	public FailedParse(Location loc)
    {
		super(loc, loc);
    }
	
	@Override
	public boolean failed()
	{
	    return true;
	}
	
	public String message()
	{
		return getClass().toString();
	}
}
