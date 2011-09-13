package com.cowlang2.parser.nodes;

import com.cowlang2.parser.core.Location;
import com.cowlang2.parser.core.ParseResult;
import com.cowlang2.parser.tokens.VariableReadNode;

public class Expression3 extends Parser
{
	public static Expression3 Instance = new Expression3();
	
	@Override
	protected ParseResult parseImpl(Location location)
	{
		ParseResult pr1 = ParenthesisedExpressionParser.Instance.parse(location);
		if (pr1.success())
			return pr1;
		
		ParseResult pr3 = IdentifierParser.Instance.parse(location);
		if (pr3.success())
			return new VariableReadNode(pr3.start(), pr3.end());
		
		return combineParseErrors(pr1, pr3);
	}
}
