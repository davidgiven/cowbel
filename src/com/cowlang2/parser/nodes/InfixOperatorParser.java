package com.cowlang2.parser.nodes;

import com.cowlang2.parser.core.Location;
import com.cowlang2.parser.core.ParseResult;
import com.cowlang2.parser.tokens.ExpressionNode;
import com.cowlang2.parser.tokens.IdentifierNode;
import com.cowlang2.parser.tokens.MethodCallNode;

public class InfixOperatorParser extends Parser
{
	@Override
	protected ParseResult parseImpl(Location location)
	{
		ParseResult left = Expression2Parser.parse(location);
		if (left.failed())
			return left;
		
		for (;;)
		{
			ParseResult operator = OperatorParser.parse(left.end());
			if (operator.failed())
				return left;

			ParseResult right = Expression2Parser.parse(operator.end());
			if (right.failed())
				return right;
		
			left = new MethodCallNode(location, right.end(),
					(ExpressionNode)left, (IdentifierNode)operator,
					(ExpressionNode)right);
			location = right.end();
		}
	}
}
