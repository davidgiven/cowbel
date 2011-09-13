package com.cowlang2.parser.nodes;

import com.cowlang2.parser.core.Location;
import com.cowlang2.parser.core.ParseResult;
import com.cowlang2.parser.errors.InvalidStatement;

public class StatementParser extends Parser
{
	public static StatementParser Instance = new StatementParser();
	
	@Override
	protected ParseResult parseImpl(Location location)
	{
		ParseResult pr = ExpressionStatementParser.Instance.parse(location);
		if (pr.failed())
			pr = new InvalidStatement(location);
		
		return pr;
	}
}
