package com.cowlang.sake.parser.nodes;

import com.cowlang.sake.parser.core.Location;
import com.cowlang.sake.parser.core.ParseResult;

public class Expression2Parser extends Parser
{
	@Override
	protected ParseResult parseImpl(Location location)
	{
		ParseResult pr1 = PrefixOperatorParser.parse(location);
		if (pr1.success())
			return pr1;
		
		ParseResult pr2 = MethodCallParser.parse(location);
		if (pr2.success())
			return pr2;
		
		ParseResult pr3 = Expression3Parser.parse(location);
		if (pr3.success())
			return pr3;
		
		return combineParseErrors(pr1, pr2, pr3);
	}
}
