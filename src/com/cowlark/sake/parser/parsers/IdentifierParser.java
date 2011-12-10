package com.cowlark.sake.parser.parsers;

import com.cowlark.sake.ast.nodes.IdentifierNode;
import com.cowlark.sake.parser.core.Location;
import com.cowlark.sake.parser.core.MutableLocation;
import com.cowlark.sake.parser.core.ParseResult;
import com.cowlark.sake.parser.errors.ExpectedIdentifier;

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
