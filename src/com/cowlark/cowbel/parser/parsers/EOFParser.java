package com.cowlark.cowbel.parser.parsers;

import com.cowlark.cowbel.parser.core.EOFToken;
import com.cowlark.cowbel.parser.core.Location;
import com.cowlark.cowbel.parser.core.ParseResult;
import com.cowlark.cowbel.parser.errors.ExpectedSyntacticElement;

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
