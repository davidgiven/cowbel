package com.cowlark.sake.parser.parsers;

import com.cowlark.sake.parser.core.Location;
import com.cowlark.sake.parser.core.ParseResult;

public class ExpressionMediumParser extends Parser
{
	@Override
	protected ParseResult parseImpl(Location location)
	{
		ParseResult pr1 = ExpressionHighParser.parse(location);
		if (pr1.success())
			return pr1;
		
		ParseResult pr2 = PrefixOperatorParser.parse(location);
		if (pr2.success())
			return pr2;

		return combineParseErrors(pr1, pr2);
	}
}
