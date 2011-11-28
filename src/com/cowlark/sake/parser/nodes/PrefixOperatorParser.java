package com.cowlark.sake.parser.nodes;

import com.cowlark.sake.parser.core.Location;
import com.cowlark.sake.parser.core.ParseResult;
import com.cowlark.sake.parser.tokens.ExpressionNode;
import com.cowlark.sake.parser.tokens.IdentifierNode;
import com.cowlark.sake.parser.tokens.MethodCallNode;

public class PrefixOperatorParser extends Parser
{
	@Override
	protected ParseResult parseImpl(Location location)
	{
		ParseResult pr1 = OperatorParser.parse(location);
		if (pr1.failed())
			return pr1;
		
		ParseResult pr2 = ExpressionMediumParser.parse(pr1.end());
		if (pr2.failed())
			return pr2;
		
		return new MethodCallNode(location, pr2.end(),
				(ExpressionNode)pr2, (IdentifierNode)pr1);
	}
}
