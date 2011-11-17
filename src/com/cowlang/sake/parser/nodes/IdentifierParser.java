package com.cowlang.sake.parser.nodes;

import com.cowlang.sake.parser.core.Location;
import com.cowlang.sake.parser.core.MutableLocation;
import com.cowlang.sake.parser.core.ParseResult;
import com.cowlang.sake.parser.errors.ExpectedIdentifier;
import com.cowlang.sake.parser.tokens.IdentifierNode;

public class IdentifierParser extends Parser
{
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
		
		ParseResult pr = AtomParser.parse(location);
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
			
			return new IdentifierNode(location, end);
		}
		
		return new ExpectedIdentifier(location);
	}
}
