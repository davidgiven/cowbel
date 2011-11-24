package com.cowlark.sake.parser.nodes;

import com.cowlark.sake.parser.core.Location;
import com.cowlark.sake.parser.core.ParseResult;
import com.cowlark.sake.parser.tokens.DummyExpressionNode;
import com.cowlark.sake.parser.tokens.ExpressionNode;

public class ParenthesisedExpressionParser extends Parser
{
	@Override
	protected ParseResult parseImpl(Location location)
	{
		ParseResult pr1 = OpenParenthesisParser.parse(location);
		if (pr1.failed())
			return pr1;
		
		ParseResult pr2 = ExpressionLowParser.parse(pr1.end());
		if (pr2.failed())
			return pr2;
		
		ParseResult pr3 = CloseParenthesisParser.parse(pr2.end());
		if (pr3.failed())
			return pr3;

		return new DummyExpressionNode(pr1.start(), pr3.end(), (ExpressionNode)pr2);
	}
}
