/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING.BSD in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel.parser.core;

public class Location implements Comparable<Location>
{
	private static int _globalid = 0;
	
	private int _id = _globalid++;
	protected String _data;
	protected String _filename;
	protected int _offset;
	protected int _lineNumber;
	protected int _lineOffset;
	
	public Location(String data, String filename)
	{
		_data = data;
		_filename = filename;
		_offset = 0;
		_lineOffset = 0;
		_lineNumber = 1;
	}
	
	public Location(Location l)
	{
		_data = l._data;
		_filename = l._filename;
		_offset = l._offset;
		_lineOffset = l._lineOffset;
		_lineNumber = l._lineNumber;
	}
	
	@Override
	public int hashCode()
	{
		return _id ^ (_offset << 16);
	}
	
	@Override
	public int compareTo(Location o)
	{
		if (_id < o._id)
			return -1;
		if (_id > o._id)
			return 1;
		if (_offset < o._offset)
			return -1;
		if (_offset > o._offset)
			return 1;
		return 0;
	}
	
	@Override
	public boolean equals(Object obj)
	{
		return (compareTo((Location)obj) == 0);
	}
	
	public int getColumn()
    {
	    return _offset - _lineOffset;
    }
	
	public int getLineNumber()
    {
	    return _lineNumber;
    }
	
	public String getFilename()
    {
	    return _filename;
    }
	
	public String locationAsString()
	{
		return _filename + ":" + _lineNumber + "." +
			(_offset - _lineOffset);
	}
	
	public int codepointAtOffset(int offset)
	{
		int d = _offset + offset;
		
		if (d >= _data.length())
			return -1;
		
		return _data.codePointAt(d);
	}
	
	public boolean matches(String s)
	{
		return _data.regionMatches(_offset, s, 0, s.length());
	}
	
	public String getText(int length)
	{
		return _data.substring(_offset, _offset+length);
	}
	
	public int calculateLengthTo(Location other)
	{
		assert(other._filename == _filename);
		return other._offset - _offset;
	}
	
	protected String shortened(int len, int max)
	{
		int remaining = _data.length() - _offset;
		if (len < remaining)
			len = remaining;
		
		if (len <= max)
			return _data.substring(_offset, _offset+len);
		else
			return _data.substring(_offset, _offset+max) + "...";
	}
	
	@Override
	public String toString()
	{
		return getClass().toString() + "=("+locationAsString()+"='" + shortened(17, 16) + "')";
	}
}
