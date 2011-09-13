package com.cowlang2.parser.nodes;

import java.util.HashMap;
import com.cowlang2.parser.core.Location;
import com.cowlang2.parser.core.MutableLocation;
import com.cowlang2.parser.core.ParseResult;

public abstract class Parser
{
	private static HashMap<Location, HashMap<Class<? extends Parser>, ParseResult>> _cache =
		new HashMap<Location, HashMap<Class<? extends Parser>, ParseResult>>(); 
		
	private static ParseResult get(Location loc, Class<? extends Parser> type)
	{
		HashMap<Class<? extends Parser>, ParseResult> m =
			_cache.get(loc);
		if (m == null)
			return null;
		return m.get(type);
	}
	
	private static void put(Location loc, Class<? extends Parser> type, ParseResult result)
	{
		HashMap<Class<? extends Parser>, ParseResult> m =
			_cache.get(loc);
		if (m == null)
		{
			m = new HashMap<Class<? extends Parser>, ParseResult>();
			_cache.put(loc, m);
		}
		
		m.put(type, result);
	}

	private void whitespace(MutableLocation loc)
	{
		for (;;)
		{
			int c = loc.codepointAtOffset(0);
			if (!Character.isSpaceChar(c))
				break;
			
			loc.advance();
		}
	}
	
	protected abstract ParseResult parseImpl(Location location);
	
	public ParseResult parse(Location location)
	{
		ParseResult pr = get(location, getClass());
		if (pr != null)
			return pr;

		if (Character.isSpaceChar(location.codepointAtOffset(0)))
		{
			MutableLocation ml = new MutableLocation(location);
			whitespace(ml);
			pr = parseImpl(ml);
		}
		else
			pr = parseImpl(location);
		
		put(location, getClass(), pr);
		return pr;
	}
	
	protected static ParseResult combineParseErrors(ParseResult... results)
	{
		return results[0];
	}
}
