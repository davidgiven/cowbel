package com.cowlang2.parser.nodes;

import com.cowlang2.parser.core.Location;
import com.cowlang2.parser.core.MutableLocation;
import com.cowlang2.parser.core.ParseResult;
import com.cowlang2.parser.errors.ExpectedIdentifier;
import com.cowlang2.parser.tokens.TextToken;

public class OperatorParser extends Parser
{
	public static OperatorParser Instance = new OperatorParser();
	
	private static boolean isOperatorChar(int c)
	{
		switch (c)
		{
			case '!':
			case '$':
			case '%':
			case '^':
			case '&':
			case '*':
			case '-':
			case '=':
			case '+':
			case '@':
			case '#':
			case '~':
			case '<':
			case '>':
			case '?':
			case '/':
			case '|':
			case '\\':
			case ']':
				return true;
				
			default:
				return false;
		}
	}
	
	@Override
	protected ParseResult parseImpl(Location location)
	{
		int c = location.codepointAtOffset(0);
		if (isOperatorChar(c))
		{
			MutableLocation end = new MutableLocation(location);
			do
			{
				end.advance();
				c = end.codepointAtOffset(0);
			}
			while (isOperatorChar(c));
			
			return new TextToken(location, end);
		}
		
		return new ExpectedIdentifier(location);
	}
}
