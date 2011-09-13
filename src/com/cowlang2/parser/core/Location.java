package com.cowlang2.parser.core;

public class Location
{
	protected String _data;
	protected String _filename;
	protected int _offset;
	protected int _lineStart;
	protected int _lineNumber;
	protected int _lineOffset;
	
	public Location(String data, String filename)
	{
		_data = data;
		_filename = filename;
		_offset = 0;
		_lineStart = 0;
		_lineNumber = 1;
	}
	
	public Location(Location l)
	{
		_data = l._data;
		_filename = l._filename;
		_offset = l._offset;
		_lineStart = l._lineStart;
		_lineNumber = l._lineNumber;
		_lineOffset = l._lineOffset;
	}
	
	@Override
	public int hashCode()
	{
		return _filename.hashCode() ^ (_offset << 16);
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
}
