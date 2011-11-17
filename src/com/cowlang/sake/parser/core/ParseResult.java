package com.cowlang.sake.parser.core;

public abstract class ParseResult extends Location
{
	private Location _end;
	
	public ParseResult(Location start, Location end)
	{
		super(start);
		_end = end;
	}
	
	abstract public boolean failed();
	
	public boolean success()
	{
		return !failed();
	}

	public Location start()
	{
		return this;
	}
	
	public Location end()
	{
		return _end;
	}
	
	public String getText()
	{
		int len = calculateLengthTo(end());
		return getText(len);
	}
}
