package com.cowlang.sake.parser.nodes;

import com.cowlang.sake.parser.core.Location;
import com.cowlang.sake.parser.core.MutableLocation;
import com.cowlang.sake.parser.core.ParseResult;
import com.cowlang.sake.parser.errors.ExpectedIdentifier;
import com.cowlang.sake.parser.tokens.IdentifierNode;

public class OperatorParser extends Parser
{
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
			
			return new IdentifierNode(location, end);
		}
		
		return new ExpectedIdentifier(location);
	}
}
