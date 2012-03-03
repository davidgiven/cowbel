/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING.BSD in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel.parser.parsers;

import java.util.Collections;
import com.cowlark.cowbel.ast.DirectFunctionCallStatementNode;
import com.cowlark.cowbel.ast.ExpressionListNode;
import com.cowlark.cowbel.ast.IdentifierListNode;
import com.cowlark.cowbel.ast.IdentifierNode;
import com.cowlark.cowbel.ast.InterfaceListNode;
import com.cowlark.cowbel.ast.Node;
import com.cowlark.cowbel.parser.core.Location;
import com.cowlark.cowbel.parser.core.ParseResult;

public class DirectFunctionCallStatementParser extends Parser
{
	/* This is an evil abuse of the parser structure: VarDeclParser
	 * needs to call in here to do initialiser parses. */
	
	ParseResult parseWithVariableList(IdentifierListNode variablespr,
			Location location)
	{
		ParseResult identifierpr = IdentifierParser.parse(location);
		if (identifierpr.failed())
			return identifierpr;

		ParseResult typeargspr = TypeListParser.parse(identifierpr.end());
		if (typeargspr.failed())
			return typeargspr;
		
		ParseResult argumentspr = ArgumentListParser.parse(typeargspr.end());
		if (argumentspr.failed())
			return argumentspr;

		/* Consume the trailing ; that distinguishes this from an expression
		 * call. */
		
		ParseResult pr = SemicolonParser.parse(argumentspr.end());
		if (pr.failed())
			return pr;
		
		return new DirectFunctionCallStatementNode(location, pr.end(),
				(IdentifierNode) identifierpr,
				(InterfaceListNode) typeargspr,
				(IdentifierListNode) variablespr,
				(ExpressionListNode) argumentspr);
	}

	@Override
	protected ParseResult parseImpl(Location location)
	{
		ParseResult variablespr = IdentifierListParser.parse(location);
		if (variablespr.failed())
		{
			/* Assume an empty list. */
			variablespr = new IdentifierListNode(location, location,
					Collections.<IdentifierNode>emptyList());
		}
		
		Location n = variablespr.end();
		if (((Node) variablespr).getNumberOfChildren() > 0)
		{
			ParseResult pr = EqualsParser.parse(variablespr.end());
			if (pr.failed())
				return pr;
			n = pr.end();
		}
		
		return parseWithVariableList((IdentifierListNode) variablespr, n);
	}
}
