package com.cowlang2.parser.nodes;

import java.util.LinkedList;
import com.cowlang2.parser.core.Location;
import com.cowlang2.parser.core.ParseResult;
import com.cowlang2.parser.tokens.ExpressionNode;
import com.cowlang2.parser.tokens.IdentifierNode;
import com.cowlang2.parser.tokens.MethodCallNode;

public class MethodCallParser extends Parser
{
	@Override
	protected ParseResult parseImpl(Location location)
	{
		ParseResult object = Expression3Parser.parse(location);
		if (object.failed())
			return object;
		
		ParseResult pr = DotParser.parse(object.end());
		if (pr.failed())
			return pr;
		
		ParseResult method = MethodNameParser.parse(pr.end());
		if (method.failed())
			return method;
		
		pr = OpenParenthesisParser.parse(method.end());
		if (pr.failed())
			return pr;
		
		LinkedList<ExpressionNode> args = new LinkedList<ExpressionNode>();
		
		pr = CloseParenthesisParser.parse(pr.end());
		if (pr.failed())
		{
			/* Argument list is not empty. */
			
			for (;;)
			{
				ParseResult arg = Expression1Parser.parse(pr.end());
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
		
		return new MethodCallNode(location, pr.end(),
				(ExpressionNode)object, (IdentifierNode)method,
				args);
	}
}
