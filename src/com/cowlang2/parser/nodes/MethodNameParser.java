package com.cowlang2.parser.nodes;

import com.cowlang2.parser.core.Location;
import com.cowlang2.parser.core.ParseResult;

public class MethodNameParser extends Parser
{
	@Override
	protected ParseResult parseImpl(Location location)
	{
		ParseResult pr1 = IdentifierParser.parse(location);
		if (pr1.success())
			return pr1;
		
		ParseResult pr2 = OperatorParser.parse(location);
		if (pr2.success())
			return pr2;
		
		return combineParseErrors(pr1, pr2);
	}
}
