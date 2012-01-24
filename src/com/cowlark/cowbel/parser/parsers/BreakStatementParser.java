package com.cowlark.cowbel.parser.parsers;

import com.cowlark.cowbel.ast.nodes.BreakStatementNode;
import com.cowlark.cowbel.parser.core.Location;
import com.cowlark.cowbel.parser.core.ParseResult;

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
