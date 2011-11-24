package com.cowlark.sake.parser.nodes;

import com.cowlark.sake.parser.core.Location;
import com.cowlark.sake.parser.core.ParseResult;
import com.cowlark.sake.parser.tokens.ExpressionStatementNode;

public class ExpressionStatementParser extends Parser
{
	@Override
	protected ParseResult parseImpl(Location location)
	{
		ParseResult pr1 = ExpressionLowParser.parse(location);
		if (pr1.failed())
			return pr1;
		
		ParseResult pr2 = SemicolonParser.parse(pr1.end());
		if (pr2.failed())
			return pr2;
		
		return new ExpressionStatementNode(location, pr2.end(), pr1);
	}
}
