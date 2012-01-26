package com.cowlark.cowbel.parser.parsers;

import java.util.Collections;
import com.cowlark.cowbel.ast.nodes.ArgumentListNode;
import com.cowlark.cowbel.ast.nodes.DirectFunctionCallStatementNode;
import com.cowlark.cowbel.ast.nodes.IdentifierListNode;
import com.cowlark.cowbel.ast.nodes.IdentifierNode;
import com.cowlark.cowbel.parser.core.Location;
import com.cowlark.cowbel.parser.core.ParseResult;

public class DirectFunctionCallStatementParser extends Parser
{
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
		
		ParseResult identifierpr = IdentifierParser.parse(variablespr.end());
		if (identifierpr.failed())
			return identifierpr;

		ParseResult argumentspr = ArgumentListParser.parse(identifierpr.end());
		if (argumentspr.failed())
			return argumentspr;

		/* Consume the trailing ; that distinguishes this from an expression
		 * call. */
		
		ParseResult pr = SemicolonParser.parse(argumentspr.end());
		if (pr.failed())
			return pr;
		
		return new DirectFunctionCallStatementNode(location, pr.end(),
				(IdentifierNode) identifierpr,
				(IdentifierListNode) variablespr,
				(ArgumentListNode) argumentspr);
	}
}
