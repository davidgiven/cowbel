package com.cowlang.sake.parser.nodes;

import com.cowlang.sake.parser.core.Location;
import com.cowlang.sake.parser.core.ParseResult;
import com.cowlang.sake.parser.tokens.ExpressionNode;
import com.cowlang.sake.parser.tokens.IdentifierNode;
import com.cowlang.sake.parser.tokens.MethodCallNode;

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
