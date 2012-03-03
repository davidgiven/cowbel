/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING.BSD in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel.parser.core;

import com.cowlark.cowbel.utils.DeterministicObject;

public abstract class ParseResult extends DeterministicObject<ParseResult>
{
	private Location _start;
	private Location _end;
	
	public ParseResult(Location start, Location end)
	{
		_start = start;
		_end = end;
	}
	
	abstract public boolean failed();
	
	public boolean success()
	{
		return !failed();
	}

	public Location start()
	{
		return _start;
	}
	
	public Location end()
	{
		return _end;
	}
	
	public void setEnd(Location end)
	{
		_end = end;
	}
	
	private String _text;
	public String getText()
	{
		if (_text == null)
		{
			int len = start().calculateLengthTo(end());
			_text = start().getText(len);
		}
		return _text;
	}
	
	public String locationAsString()
	{
		return start().locationAsString();
	}
		
	@Override
	public String toString()
	{
		Location loc = start();
		int len = loc.calculateLengthTo(end());
		return getClass().toString() + "=("+loc.locationAsString()+"='" + loc.shortened(len, 16) + "')";
	}
}
