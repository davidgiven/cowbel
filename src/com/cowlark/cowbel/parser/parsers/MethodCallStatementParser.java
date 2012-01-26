package com.cowlark.cowbel.parser.parsers;

import com.cowlark.cowbel.ast.nodes.ArgumentListNode;
import com.cowlark.cowbel.ast.nodes.ExpressionNode;
import com.cowlark.cowbel.ast.nodes.IdentifierListNode;
import com.cowlark.cowbel.ast.nodes.IdentifierNode;
import com.cowlark.cowbel.ast.nodes.IndirectFunctionCallExpressionNode;
import com.cowlark.cowbel.ast.nodes.IndirectFunctionCallStatementNode;
import com.cowlark.cowbel.ast.nodes.MethodCallExpressionNode;
import com.cowlark.cowbel.ast.nodes.MethodCallStatementNode;
import com.cowlark.cowbel.parser.core.Location;
import com.cowlark.cowbel.parser.core.ParseResult;

public class MethodCallStatementParser extends Parser
{
	@Override
	protected ParseResult parseImpl(Location location)
	{
		ParseResult variablespr = IdentifierListParser.parse(location);
		if (variablespr.failed())
		{
			/* Assume an empty list. */
			variablespr = new IdentifierListNode(location, location);
		}
		
		ParseResult seed = ExpressionLeafParser.parse(variablespr.end());
		if (seed.failed())
			return seed;
		
		for (;;)
		{
			ParseResult pr = DotParser.parse(seed.end());
			if (pr.success())
			{
				ParseResult identifierpr = IdentifierParser.parse(pr.end());
				if (identifierpr.failed())
					return identifierpr;
				
				ParseResult argumentspr = ArgumentListParser.parse(identifierpr.end());
				if (argumentspr.failed())
					return argumentspr;
				
				/* Is this the end of a method call statement? */
				
				pr = SemicolonParser.parse(argumentspr.end());
				if (pr.success())
				{
					return new MethodCallStatementNode(
							identifierpr, pr.end(),
							(ExpressionNode) seed,
							(IdentifierNode) identifierpr,
							(IdentifierListNode) variablespr,
							(ArgumentListNode) argumentspr);
				}
				
				/* If not, add it to the seed and go around again. */
				
				seed = new MethodCallExpressionNode(
						identifierpr, argumentspr.end(),
						(ExpressionNode) seed,
						(IdentifierNode) identifierpr,
						(ArgumentListNode) argumentspr);
				continue;
			}
			
			/* Otherwise this could be a indirect function call statement. */
			
			ParseResult argumentspr = ArgumentListParser.parse(seed.end());
			if (argumentspr.failed())
				return argumentspr;
			
			/* Is this the end of the statement? */
			
			pr = SemicolonParser.parse(argumentspr.end());
			if (pr.success())
			{
				return new IndirectFunctionCallStatementNode(
						location, pr.end(),
						(ExpressionNode) seed,
						(IdentifierListNode) variablespr,
						(ArgumentListNode) argumentspr);
			}
			
			/* Otherwise, update the seed and go around again. */
			
			seed = new IndirectFunctionCallExpressionNode(
					argumentspr, argumentspr.end(),
					(ExpressionNode) seed,
					(ArgumentListNode) argumentspr);
		}
	}
}
