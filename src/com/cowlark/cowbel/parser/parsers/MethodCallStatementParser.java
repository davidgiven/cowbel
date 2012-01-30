/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel.parser.parsers;

import com.cowlark.cowbel.ast.nodes.ExpressionListNode;
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
	/* This is an evil abuse of the parser structure: VarDeclParser
	 * needs to call in here to do initialiser parses. */
	
	ParseResult parseWithVariableList(IdentifierListNode variablespr,
			Location location)
	{
		ParseResult seed = ExpressionLeafParser.parse(location);
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
							(ExpressionListNode) argumentspr);
				}
				
				/* If not, add it to the seed and go around again. */
				
				seed = new MethodCallExpressionNode(
						identifierpr, argumentspr.end(),
						(ExpressionNode) seed,
						(IdentifierNode) identifierpr,
						(ExpressionListNode) argumentspr);
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
						(ExpressionListNode) argumentspr);
			}
			
			/* Otherwise, update the seed and go around again. */
			
			seed = new IndirectFunctionCallExpressionNode(
					argumentspr, argumentspr.end(),
					(ExpressionNode) seed,
					(ExpressionListNode) argumentspr);
		}
	}

	@Override
	protected ParseResult parseImpl(Location location)
	{
		ParseResult variablespr = IdentifierListParser.parse(location);
		if (variablespr.failed())
		{
			/* Assume an empty list. */
			variablespr = new IdentifierListNode(location, location);
		}

		return parseWithVariableList((IdentifierListNode) variablespr,
				variablespr.end());
	}
}
