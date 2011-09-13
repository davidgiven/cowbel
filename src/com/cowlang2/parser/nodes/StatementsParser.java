package com.cowlang2.parser.nodes;

import com.cowlang2.parser.core.Location;
import com.cowlang2.parser.core.ParseResult;

public class StatementsParser extends Parser
{
	public static StatementsParser Instance = new StatementsParser();
	
	@Override
	protected ParseResult parseImpl(Location location)
	{
		return StatementParser.Instance.parse(location); 
	}
}
