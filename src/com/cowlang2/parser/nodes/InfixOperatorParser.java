package com.cowlang2.parser.nodes;

import com.cowlang2.parser.core.Location;
import com.cowlang2.parser.core.ParseResult;
import com.cowlang2.parser.tokens.ExpressionNode;
import com.cowlang2.parser.tokens.MethodCallNode;
import com.cowlang2.parser.tokens.TextToken;

public class InfixOperatorParser extends Parser
{
	public static InfixOperatorParser Instance = new InfixOperatorParser();
	
	@Override
	protected ParseResult parseImpl(Location location)
	{
		ParseResult left = Expression2.Instance.parse(location);
		if (left.failed())
			return left;
		
		for (;;)
		{
			ParseResult operator = OperatorParser.Instance.parse(left.end());
			if (operator.failed())
				return left;

			ParseResult right = Expression2.Instance.parse(operator.end());
			if (right.failed())
				return right;
		
			left = new MethodCallNode(location, right.end(),
					(ExpressionNode)left, (TextToken)operator, (ExpressionNode)right);
			location = right.end();
		}
	}
}
