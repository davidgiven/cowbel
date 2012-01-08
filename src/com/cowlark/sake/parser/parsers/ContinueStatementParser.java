package com.cowlark.sake.parser.parsers;

import com.cowlark.sake.ast.nodes.ContinueStatementNode;
import com.cowlark.sake.parser.core.Location;
import com.cowlark.sake.parser.core.ParseResult;

public class ContinueStatementParser extends Parser
{
	@Override
	protected ParseResult parseImpl(Location location)
	{
		ParseResult pr = ContinueTokenParser.parse(location);
		if (pr.failed())
			return pr;
		
		pr = SemicolonParser.parse(pr.end());
		if (pr.failed())
			return pr;

		return new ContinueStatementNode(location, pr.end());
	}
}
