package com.cowlang2.parser.nodes;

import com.cowlang2.parser.core.Location;
import com.cowlang2.parser.core.ParseResult;
import com.cowlang2.parser.tokens.ExpressionStatementNode;

public class ExpressionStatementParser extends Parser
{
	public static ExpressionStatementParser Instance = new ExpressionStatementParser();
	
	@Override
	protected ParseResult parseImpl(Location location)
	{
		ParseResult pr1 = Expression1.Instance.parse(location);
		if (pr1.failed())
			return pr1;
		
		ParseResult pr2 = SemicolonParser.Instance.parse(pr1.end());
		if (pr2.failed())
			return pr2;
		
		return new ExpressionStatementNode(location, pr2.end(), pr1);
	}
}
