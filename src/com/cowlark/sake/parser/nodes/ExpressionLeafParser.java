package com.cowlark.sake.parser.nodes;

import com.cowlark.sake.ast.nodes.VariableReadNode;
import com.cowlark.sake.parser.core.Location;
import com.cowlark.sake.parser.core.ParseResult;

public class ExpressionLeafParser extends Parser
{
	@Override
	protected ParseResult parseImpl(Location location)
	{
		ParseResult pr1 = ParenthesisedExpressionParser.parse(location);
		if (pr1.success())
			return pr1;
		
		ParseResult pr2 = IdentifierParser.parse(location);
		if (pr2.success())
			return new VariableReadNode(pr2.start(), pr2.end());
		
		ParseResult pr3 = StringConstantParser.parse(location);
		if (pr3.success())
			return pr3;
		
		ParseResult pr4 = ListConstantParser.parse(location);
		if (pr4.success())
			return pr4;
		
		return combineParseErrors(pr1, pr2, pr3, pr4);
	}
}
