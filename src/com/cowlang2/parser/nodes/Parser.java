package com.cowlang2.parser.nodes;

import java.util.HashMap;
import com.cowlang2.parser.core.Location;
import com.cowlang2.parser.core.MutableLocation;
import com.cowlang2.parser.core.ParseResult;

public abstract class Parser
{
	private static HashMap<Location, HashMap<Parser, ParseResult>> _cache =
		new HashMap<Location, HashMap<Parser, ParseResult>>(); 
		
	private static ParseResult get(Location loc, Parser type)
	{
		HashMap<Parser, ParseResult> m = _cache.get(loc);
		if (m == null)
			return null;
		return m.get(type);
	}
	
	private static void put(Location loc, Parser type, ParseResult result)
	{
		HashMap<Parser, ParseResult> m = _cache.get(loc);
		if (m == null)
		{
			m = new HashMap<Parser, ParseResult>();
			_cache.put(loc, m);
		}
		
		m.put(type, result);
	}

	private void linecomment(MutableLocation loc)
	{
		loc.advance(2); /* Skip the // */
		
		for (;;)
		{
			int c = loc.codepointAtOffset(0);
			if ((c == '\n') || (c == -1))
				break;
			
			loc.advance();
		}
	}
	
	private void blockcomment(MutableLocation loc)
	{
		loc.advance(2); /* Skip the /* */
		
		for (;;)
		{
			int c1 = loc.codepointAtOffset(0);
			int c2 = loc.codepointAtOffset(1);
			
			if ((c1 == '*') && (c2 == '/'))
			{
				loc.advance(2);
				break;
			}
			
			if (c1 == -1)
				break;
			
			loc.advance();
		}
	}
	
	private void whitespace(MutableLocation loc)
	{
		for (;;)
		{
			int c = loc.codepointAtOffset(0);
			if (!Character.isWhitespace(c))
			{
				if (c == '/')
				{
					c = loc.codepointAtOffset(1);
					if (c == '*')
						blockcomment(loc);
					else if (c == '/')
						linecomment(loc);
					else
						break;
				}
				else
					break;
			}
			else
				loc.advance();
		}
	}
	
	protected abstract ParseResult parseImpl(Location location);
	
	public ParseResult parse(Location location)
	{
		ParseResult pr = get(location, this);
		if (pr != null)
			return pr;

		int c = location.codepointAtOffset(0);
		if (Character.isWhitespace(c) || (c == '/'))
		{
			MutableLocation ml = new MutableLocation(location);
			whitespace(ml);
			pr = parseImpl(ml);
		}
		else
			pr = parseImpl(location);
		
		put(location, this, pr);
		return pr;
	}
	
	protected static ParseResult combineParseErrors(ParseResult... results)
	{
		return results[0];
	}
}
