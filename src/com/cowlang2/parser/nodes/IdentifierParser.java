package com.cowlang2.parser.nodes;

import com.cowlang2.parser.core.Location;
import com.cowlang2.parser.core.MutableLocation;
import com.cowlang2.parser.core.ParseResult;
import com.cowlang2.parser.errors.ExpectedIdentifier;
import com.cowlang2.parser.tokens.TextToken;

public class IdentifierParser extends Parser
{
	public static IdentifierParser Instance = new IdentifierParser();
	
	private static boolean isWordFirstChar(int c)
	{
		if (Character.isLetter(c))
			return true;
		if (c == '_')
			return true;
		return false;
	}
	
	private static boolean isWordSubsequentChar(int c)
	{
		if (Character.isDigit(c))
			return true;
		return isWordFirstChar(c);
	}
	
	@Override
	protected ParseResult parseImpl(Location location)
	{
		/* This must not be a keyword. */
		
		ParseResult pr = AtomParser.Instance.parse(location);
		if (pr.success())
			return new ExpectedIdentifier(location);
		
		int c = location.codepointAtOffset(0);
		if (isWordFirstChar(c))
		{
			MutableLocation end = new MutableLocation(location);
			do
			{
				end.advance();
				c = end.codepointAtOffset(0);
			}
			while (isWordSubsequentChar(c));
			
			return new TextToken(location, end);
		}
		
		return new ExpectedIdentifier(location);
	}
}
