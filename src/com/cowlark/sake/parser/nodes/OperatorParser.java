package com.cowlark.sake.parser.nodes;

import com.cowlark.sake.ast.nodes.IdentifierNode;
import com.cowlark.sake.parser.core.Location;
import com.cowlark.sake.parser.core.MutableLocation;
import com.cowlark.sake.parser.core.ParseResult;
import com.cowlark.sake.parser.errors.ExpectedIdentifier;

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
