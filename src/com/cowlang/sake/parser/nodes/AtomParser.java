package com.cowlang.sake.parser.nodes;

import com.cowlang.sake.parser.core.Location;
import com.cowlang.sake.parser.core.MutableLocation;
import com.cowlang.sake.parser.core.ParseResult;
import com.cowlang.sake.parser.errors.ExpectedAtom;
import com.cowlang.sake.parser.tokens.SyntacticElementToken;

public class AtomParser extends Parser
{
	@Override
	protected ParseResult parseImpl(Location location)
	{
		for (char keyword : S.SingleCharOperators)
		{
			if (location.codepointAtOffset(0) == keyword)
			{
				MutableLocation end = new MutableLocation(location);
				end.advance();
				return new SyntacticElementToken(location, end);
			}
		}
		
		for (String keyword : S.Keywords)
		{
			if (location.matches(keyword))
			{
				int c = location.codepointAtOffset(keyword.length());
				if (!Character.isJavaIdentifierPart(c))
				{
					MutableLocation end = new MutableLocation(location);
					end.advance(keyword.length());
					return new SyntacticElementToken(location, end);
				}
			}
		}
		
		return new ExpectedAtom(location);
	}
}
