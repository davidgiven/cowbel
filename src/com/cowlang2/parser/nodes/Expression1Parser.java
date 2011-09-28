package com.cowlang2.parser.nodes;

import com.cowlang2.parser.core.Location;
import com.cowlang2.parser.core.ParseResult;

public class Expression1Parser extends Parser
{
	@Override
	protected ParseResult parseImpl(Location location)
	{
		ParseResult pr1 = InfixOperatorParser.parse(location);
		if (pr1.success())
			return pr1;
		
		return combineParseErrors(pr1);
	}
}
