package com.cowlang2.parser.nodes;

import com.cowlang2.parser.core.Location;
import com.cowlang2.parser.core.ParseResult;

public class Expression2 extends Parser
{
	public static Expression2 Instance = new Expression2();
	
	@Override
	protected ParseResult parseImpl(Location location)
	{
		ParseResult pr1 = PrefixOperatorParser.Instance.parse(location);
		if (pr1.success())
			return pr1;
		
		ParseResult pr2 = MethodCallParser.Instance.parse(location);
		if (pr2.success())
			return pr2;
		
		ParseResult pr3 = Expression3.Instance.parse(location);
		if (pr3.success())
			return pr3;
		
		return combineParseErrors(pr1, pr2, pr3);
	}
}
