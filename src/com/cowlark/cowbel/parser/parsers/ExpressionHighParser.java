package com.cowlark.cowbel.parser.parsers;

import java.util.LinkedList;
import com.cowlark.cowbel.ast.nodes.DirectFunctionCallNode;
import com.cowlark.cowbel.ast.nodes.ExpressionNode;
import com.cowlark.cowbel.ast.nodes.FunctionCallNode;
import com.cowlark.cowbel.ast.nodes.IdentifierNode;
import com.cowlark.cowbel.ast.nodes.MethodCallNode;
import com.cowlark.cowbel.parser.core.Location;
import com.cowlark.cowbel.parser.core.ParseResult;

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
	
	private ParseResult parseDirectFunctionCall(Location location)
	{
		LinkedList<ExpressionNode> args = new LinkedList<ExpressionNode>();
		
		ParseResult identifierpr = IdentifierParser.parse(location);
		if (identifierpr.failed())
			return identifierpr;
		
		ParseResult pr = OpenParenthesisParser.parse(identifierpr.end());
		if (pr.failed())
			return pr;
		
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
		
		return new DirectFunctionCallNode(location, pr.end(),
				(IdentifierNode)identifierpr, args);
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
