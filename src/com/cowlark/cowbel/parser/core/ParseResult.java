package com.cowlark.cowbel.parser.core;

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
	
	private String _text;
	public String getText()
	{
		if (_text == null)
		{
			int len = calculateLengthTo(end());
			_text = getText(len);
		}
		return _text;
	}
	
	
	@Override
	public String toString()
	{
		int len = calculateLengthTo(end());
		return getClass().toString() + "=("+locationAsString()+"='" + shortened(len, 16) + "')";
	}
}
