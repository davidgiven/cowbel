package com.cowlark.sake.parser.nodes;

import com.cowlark.sake.parser.core.Location;
import com.cowlark.sake.parser.core.MutableLocation;
import com.cowlark.sake.parser.core.ParseResult;
import com.cowlark.sake.parser.errors.ExpectedAtom;
import com.cowlark.sake.parser.tokens.SyntacticElementToken;

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
