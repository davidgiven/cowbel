package com.cowlang.sake.parser.nodes;

import java.util.LinkedList;
import com.cowlang.sake.parser.core.Location;
import com.cowlang.sake.parser.core.ParseResult;
import com.cowlang.sake.parser.tokens.ExpressionNode;
import com.cowlang.sake.parser.tokens.IdentifierNode;
import com.cowlang.sake.parser.tokens.FunctionCallNode;

public class FunctionCallParser extends Parser
{
	@Override
	protected ParseResult parseImpl(Location location)
	{
		ParseResult function = ExpressionLeafParser.parse(location);
		if (function.failed())
			return function;
		
		ParseResult pr = OpenParenthesisParser.parse(function.end());
		if (pr.failed())
			return pr;
		
		LinkedList<ExpressionNode> args = new LinkedList<ExpressionNode>();
		
		pr = CloseParenthesisParser.parse(pr.end());
		if (pr.failed())
		{
			/* Argument list is not empty. */
			
			for (;;)
			{
				ParseResult arg = ExpressionLowParser.parse(pr.end());
				if (arg.failed())
					return arg;
				args.addLast((ExpressionNode)arg);
				
				pr = CloseParenthesisParser.parse(arg.end());
				if (pr.success())
					break;
				
				pr = CommaParser.parse(arg.end());
				if (pr.failed())
					return pr;
			}
		}
		
		return new FunctionCallNode(location, pr.end(),
				(ExpressionNode)function, args);
	}
}
