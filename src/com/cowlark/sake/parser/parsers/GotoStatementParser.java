package com.cowlark.sake.parser.parsers;

import com.cowlark.sake.ast.nodes.GotoStatementNode;
import com.cowlark.sake.ast.nodes.IdentifierNode;
import com.cowlark.sake.parser.core.Location;
import com.cowlark.sake.parser.core.ParseResult;

public class GotoStatementParser extends Parser
{
	@Override
	protected ParseResult parseImpl(Location location)
	{
		ParseResult pr = GotoTokenParser.parse(location);
		if (pr.failed())
			return pr;
		
		ParseResult identifierpr = IdentifierParser.parse(pr.end());
		if (identifierpr.failed())
			return identifierpr;
		
		pr = SemicolonParser.parse(identifierpr.end());
		if (pr.failed())
			return pr;

		return new GotoStatementNode(location, pr.end(),
				(IdentifierNode) identifierpr);
	}
}
