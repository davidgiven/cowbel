package com.cowlark.cowbel.parser.parsers;

import java.util.LinkedList;
import com.cowlark.cowbel.ast.nodes.ArgumentListNode;
import com.cowlark.cowbel.ast.nodes.DirectFunctionCallExpressionNode;
import com.cowlark.cowbel.ast.nodes.ExpressionNode;
import com.cowlark.cowbel.ast.nodes.IdentifierNode;
import com.cowlark.cowbel.ast.nodes.IndirectFunctionCallExpressionNode;
import com.cowlark.cowbel.ast.nodes.MethodCallExpressionNode;
import com.cowlark.cowbel.parser.core.Location;
import com.cowlark.cowbel.parser.core.ParseResult;

public class ExpressionHighParser extends Parser
{
	private ParseResult parseMethodCall(ParseResult seed, Location location)
	{
		ParseResult method = MethodNameParser.parse(location);
		if (method.failed())
			return method;
		
		ParseResult arguments = ArgumentListParser.parse(method.end());
		if (arguments.failed())
			return arguments;
		
		return new MethodCallExpressionNode(location, arguments.end(),
				(ExpressionNode) seed, (IdentifierNode) method,
				(ArgumentListNode) arguments);
	}
	
	private ParseResult parseFunctionCall(ParseResult seed, Location location)
	{
		ParseResult arguments = ArgumentListParser.parse(location);
		if (arguments.failed())
			return arguments;
		
		return new IndirectFunctionCallExpressionNode(location, arguments.end(),
				(ExpressionNode)seed, (ArgumentListNode) arguments);
	}
	
	private ParseResult parseDirectFunctionCall(Location location)
	{
		LinkedList<ExpressionNode> args = new LinkedList<ExpressionNode>();
		
		ParseResult identifierpr = IdentifierParser.parse(location);
		if (identifierpr.failed())
			return identifierpr;
		
		ParseResult argumentspr = ArgumentListParser.parse(identifierpr.end());
		if (argumentspr.failed())
			return argumentspr;
		
		return new DirectFunctionCallExpressionNode(location, argumentspr.end(),
				(IdentifierNode) identifierpr,
				(ArgumentListNode) argumentspr);
	}
	
	@Override
	protected ParseResult parseImpl(Location location)
	{
		ParseResult seed = parseDirectFunctionCall(location);
		if (seed.failed())
			seed = ExpressionLeafParser.parse(location);
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
