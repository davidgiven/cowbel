package com.cowlang2.parser.nodes;

import com.cowlang2.parser.core.Location;
import com.cowlang2.parser.core.ParseResult;

public class Expression1 extends Parser
{
	public static Expression1 Instance = new Expression1();
	
	@Override
	protected ParseResult parseImpl(Location location)
	{
		ParseResult pr1 = InfixOperatorParser.Instance.parse(location);
		if (pr1.success())
			return pr1;
		
		return combineParseErrors(pr1);
	}
}
