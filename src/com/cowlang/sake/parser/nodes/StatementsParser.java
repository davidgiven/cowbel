package com.cowlang.sake.parser.nodes;

import com.cowlang.sake.parser.core.Location;
import com.cowlang.sake.parser.core.ParseResult;

public class StatementsParser extends Parser
{
	@Override
	protected ParseResult parseImpl(Location location)
	{
		return StatementParser.parse(location); 
	}
}
