package com.cowlark.cowbel.parser.parsers;

import com.cowlark.cowbel.ast.nodes.IdentifierNode;
import com.cowlark.cowbel.parser.core.Location;
import com.cowlark.cowbel.parser.core.MutableLocation;
import com.cowlark.cowbel.parser.core.ParseResult;
import com.cowlark.cowbel.parser.errors.ExpectedIdentifier;

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
