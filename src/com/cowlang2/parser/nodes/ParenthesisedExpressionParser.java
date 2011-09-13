package com.cowlang2.parser.nodes;

import com.cowlang2.parser.core.Location;
import com.cowlang2.parser.core.ParseResult;
import com.cowlang2.parser.tokens.DummyExpressionNode;
import com.cowlang2.parser.tokens.ExpressionNode;

public class ParenthesisedExpressionParser extends Parser
{
	public static ParenthesisedExpressionParser Instance = new ParenthesisedExpressionParser();
	
	@Override
	protected ParseResult parseImpl(Location location)
	{
		ParseResult pr1 = OpenParenthesisParser.Instance.parse(location);
		if (pr1.failed())
			return pr1;
		
		ParseResult pr2 = Expression1.Instance.parse(pr1.end());
		if (pr2.failed())
			return pr2;
		
		ParseResult pr3 = CloseParenthesisParser.Instance.parse(pr2.end());
		if (pr3.failed())
			return pr3;

		return new DummyExpressionNode(pr1.start(), pr3.end(), (ExpressionNode)pr2);
	}
}
