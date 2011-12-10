package com.cowlark.sake.parser.parsers;

import com.cowlark.sake.parser.core.EOFToken;
import com.cowlark.sake.parser.core.Location;
import com.cowlark.sake.parser.core.ParseResult;
import com.cowlark.sake.parser.errors.ExpectedSyntacticElement;

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
