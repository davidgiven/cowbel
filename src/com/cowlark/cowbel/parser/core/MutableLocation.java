/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING.BSD in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel.parser.core;

public class MutableLocation extends Location
{
	public MutableLocation(Location loc)
    {
	    super(loc);
    }
	
	public void setLineNumber(int i)
	{
		_lineNumber = i;
	}
	
	public void setFilename(String f)
	{
		_filename = f;
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
