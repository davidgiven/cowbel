package com.cowlang2.parser.nodes;

import com.cowlang2.parser.core.Location;
import com.cowlang2.parser.core.ParseResult;
import com.cowlang2.parser.tokens.ExpressionNode;
import com.cowlang2.parser.tokens.MethodCallNode;
import com.cowlang2.parser.tokens.TextToken;

public class PrefixOperatorParser extends Parser
{
	@Override
	protected ParseResult parseImpl(Location location)
	{
		ParseResult pr1 = OperatorParser.parse(location);
		if (pr1.failed())
			return pr1;
		
		ParseResult pr2 = Expression3Parser.parse(pr1.end());
		if (pr2.failed())
			return pr2;
		
		return new MethodCallNode(location, pr2.end(),
				(ExpressionNode)pr2, (TextToken)pr1);
	}
}
