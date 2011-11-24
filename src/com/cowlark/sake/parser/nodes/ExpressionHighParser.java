package com.cowlark.sake.parser.nodes;

import com.cowlark.sake.parser.core.Location;
import com.cowlark.sake.parser.core.ParseResult;

public class ExpressionHighParser extends Parser
{
	@Override
	protected ParseResult parseImpl(Location location)
	{
		ParseResult pr2 = FunctionCallParser.parse(location);
		if (pr2.success())
			return pr2;
		
		ParseResult pr3 = ExpressionLeafParser.parse(location);
		if (pr3.success())
			return pr3;
		
		return combineParseErrors(pr2, pr3);
	}
}
