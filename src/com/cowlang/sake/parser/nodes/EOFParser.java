package com.cowlang.sake.parser.nodes;

import com.cowlang.sake.parser.core.Location;
import com.cowlang.sake.parser.core.ParseResult;
import com.cowlang.sake.parser.errors.ExpectedSyntacticElement;
import com.cowlang.sake.parser.tokens.EOFToken;

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
