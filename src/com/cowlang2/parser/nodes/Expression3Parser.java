package com.cowlang2.parser.nodes;

import com.cowlang2.parser.core.Location;
import com.cowlang2.parser.core.ParseResult;
import com.cowlang2.parser.tokens.VariableReadNode;

public class Expression3Parser extends Parser
{
	@Override
	protected ParseResult parseImpl(Location location)
	{
		ParseResult pr1 = ParenthesisedExpressionParser.parse(location);
		if (pr1.success())
			return pr1;
		
		ParseResult pr3 = IdentifierParser.parse(location);
		if (pr3.success())
			return new VariableReadNode(pr3.start(), pr3.end());
		
		return combineParseErrors(pr1, pr3);
	}
}
