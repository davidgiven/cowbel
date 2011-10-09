package com.cowlang2.parser.nodes;

import com.cowlang2.parser.core.Location;
import com.cowlang2.parser.core.ParseResult;
import com.cowlang2.parser.errors.ExpectedSyntacticElement;
import com.cowlang2.parser.tokens.EOFToken;

public class EOFParser extends Parser
{
	@Override
	protected ParseResult parseImpl(Location location)
	{
		if (location.codepointAtOffset(0) == -1)
			return new EOFToken(location);
		
		return new ExpectedSyntacticElement(location, "<eof>");
	}
}
