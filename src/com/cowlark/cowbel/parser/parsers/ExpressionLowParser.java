package com.cowlark.cowbel.parser.parsers;

import com.cowlark.cowbel.ast.nodes.ExpressionNode;
import com.cowlark.cowbel.ast.nodes.IdentifierNode;
import com.cowlark.cowbel.ast.nodes.MethodCallNode;
import com.cowlark.cowbel.parser.core.Location;
import com.cowlark.cowbel.parser.core.ParseResult;

public class ExpressionLowParser extends Parser
{
	private ParseResult parseInfixMethodCall(ParseResult seed,
			ParseResult operator)
	{
		ParseResult right = ExpressionMediumParser.parse(operator.end());
		if (right.failed())
			return right;
		
		return new MethodCallNode(seed.start(), right.end(),
				(ExpressionNode) seed, (IdentifierNode) operator,
				(ExpressionNode) right);
	}
	
	@Override
	protected ParseResult parseImpl(Location location)
	{
		ParseResult seed = ExpressionMediumParser.parse(location);
		if (seed.failed())
			return seed;
		
		for (;;)
		{
			ParseResult pr = OperatorParser.parse(seed.end());
			if (pr.success())
			{
				seed = parseInfixMethodCall(seed, pr);
				if (seed.failed())
					return seed;
				continue;
			}
			
			break;
		}
		
		return seed;
	}

}
