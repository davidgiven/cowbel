package com.cowlang2.parser.core;

public class MutableLocation extends Location
{
	public MutableLocation(Location loc)
    {
	    super(loc);
    }
	
	public void advance()
	{
		if (_offset >= _data.length())
			return;
		
		int c = _data.codePointAt(_offset);
		if (Character.isSupplementaryCodePoint(c))
			_offset += 2;
		else
			_offset += 1;
		
		if (c == '\n')
		{
			_lineNumber++;
			_lineOffset = _offset;
		}
	}
	
	public void advance(int count)
	{
		for (int i=0; i<count; i++)
			advance();
	}
}
