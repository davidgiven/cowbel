package com.cowlang.sake.parser.nodes;

import com.cowlang.sake.parser.core.Location;
import com.cowlang.sake.parser.core.ParseResult;

public class ExpressionMediumParser extends Parser
{
	@Override
	protected ParseResult parseImpl(Location location)
	{
		ParseResult pr1 = ExpressionHighParser.parse(location);
		if (pr1.success())
			return pr1;
		
		return combineParseErrors(pr1);
	}
}
