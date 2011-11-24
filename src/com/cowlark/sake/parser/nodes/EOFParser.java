package com.cowlark.sake.parser.nodes;

import com.cowlark.sake.parser.core.Location;
import com.cowlark.sake.parser.core.ParseResult;
import com.cowlark.sake.parser.errors.ExpectedSyntacticElement;
import com.cowlark.sake.parser.tokens.EOFToken;

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
