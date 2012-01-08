package com.cowlark.sake.parser.parsers;

import com.cowlark.sake.ast.nodes.BreakStatementNode;
import com.cowlark.sake.parser.core.Location;
import com.cowlark.sake.parser.core.ParseResult;

public class BreakStatementParser extends Parser
{
	@Override
	protected ParseResult parseImpl(Location location)
	{
		ParseResult pr = BreakTokenParser.parse(location);
		if (pr.failed())
			return pr;
		
		pr = SemicolonParser.parse(pr.end());
		if (pr.failed())
			return pr;

		return new BreakStatementNode(location, pr.end());
	}
}
