/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel.parser.parsers;

import java.util.LinkedList;
import com.cowlark.cowbel.ast.nodes.DirectFunctionCallExpressionNode;
import com.cowlark.cowbel.ast.nodes.ExpressionListNode;
import com.cowlark.cowbel.ast.nodes.ExpressionNode;
import com.cowlark.cowbel.ast.nodes.IdentifierNode;
import com.cowlark.cowbel.ast.nodes.IndirectFunctionCallExpressionNode;
import com.cowlark.cowbel.ast.nodes.MethodCallExpressionNode;
import com.cowlark.cowbel.ast.nodes.TypeListNode;
import com.cowlark.cowbel.parser.core.Location;
import com.cowlark.cowbel.parser.core.ParseResult;

public class ExpressionHighParser extends Parser
{
	private ParseResult parseMethodCall(ParseResult seed, Location location)
	{
		ParseResult methodpr = MethodNameParser.parse(location);
		if (methodpr.failed())
			return methodpr;
		
		ParseResult typeargspr = TypeListParser.parse(methodpr.end());
		if (typeargspr.failed())
			return typeargspr;
		
		ParseResult arguments = ArgumentListParser.parse(typeargspr.end());
		if (arguments.failed())
			return arguments;
		
		return new MethodCallExpressionNode(location, arguments.end(),
				(ExpressionNode) seed, (IdentifierNode) methodpr,
				(TypeListNode) typeargspr,
				(ExpressionListNode) arguments);
	}
	
	private ParseResult parseFunctionCall(ParseResult seed, Location location)
	{
		ParseResult typeargspr = TypeListParser.parse(location);
		if (typeargspr.failed())
			return typeargspr;
		
		ParseResult argumentspr = ArgumentListParser.parse(typeargspr.end());
		if (argumentspr.failed())
			return argumentspr;
		
		return new IndirectFunctionCallExpressionNode(location, argumentspr.end(),
				(ExpressionNode)seed, (ExpressionListNode) argumentspr);
	}
	
	private ParseResult parseDirectFunctionCall(Location location)
	{
		LinkedList<ExpressionNode> args = new LinkedList<ExpressionNode>();
		
		ParseResult identifierpr = IdentifierParser.parse(location);
		if (identifierpr.failed())
			return identifierpr;
		
		ParseResult typeargspr = TypeListParser.parse(identifierpr.end());
		if (typeargspr.failed())
			return typeargspr;
		
		ParseResult argumentspr = ArgumentListParser.parse(typeargspr.end());
		if (argumentspr.failed())
			return argumentspr;
		
		return new DirectFunctionCallExpressionNode(location, argumentspr.end(),
				(IdentifierNode) identifierpr,
				(TypeListNode) typeargspr,
				(ExpressionListNode) argumentspr);
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
			
			pr = OpenAngleBracketParser.parse(seed.end());
			if (pr.success())
			{
				seed = parseFunctionCall(seed, pr.end());
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
