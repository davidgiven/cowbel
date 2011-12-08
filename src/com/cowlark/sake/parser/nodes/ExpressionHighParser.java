package com.cowlark.sake.parser.nodes;

import java.util.LinkedList;
import com.cowlark.sake.ast.nodes.ExpressionNode;
import com.cowlark.sake.ast.nodes.FunctionCallNode;
import com.cowlark.sake.ast.nodes.IdentifierNode;
import com.cowlark.sake.ast.nodes.MethodCallNode;
import com.cowlark.sake.parser.core.Location;
import com.cowlark.sake.parser.core.ParseResult;

public class ExpressionHighParser extends Parser
{
	private ParseResult parseMethodCall(ParseResult seed, Location location)
	{
		ParseResult method = MethodNameParser.parse(location);
		if (method.failed())
			return method;
		
		ParseResult pr = OpenParenthesisParser.parse(method.end());
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
		
		return new MethodCallNode(location, pr.end(),
				(ExpressionNode)seed, (IdentifierNode)method,
				args);
	}
	
	private ParseResult parseFunctionCall(ParseResult seed, Location location)
	{
		LinkedList<ExpressionNode> args = new LinkedList<ExpressionNode>();
		
		ParseResult pr = CloseParenthesisParser.parse(location);
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
				(ExpressionNode)seed, args);
	}
	
	@Override
	protected ParseResult parseImpl(Location location)
	{
		ParseResult seed = ExpressionLeafParser.parse(location);
		if (seed.failed())
			return seed;
		
		for (;;)
		{
			ParseResult pr = DotParser.parse(seed.end());
			if (pr.success())
			{
				seed = parseMethodCall(seed, pr.end());
				if (seed.failed())
					return seed;
				continue;
			}
			
			pr = OpenParenthesisParser.parse(seed.end());
			if (pr.success())
			{
				seed = parseFunctionCall(seed, pr.end());
				if (seed.failed())
					return seed;
				continue;
			}
			
			break;
		}
		
		return seed;
	}
}
