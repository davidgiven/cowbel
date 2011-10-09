package com.cowlang2.parser.nodes;

import com.cowlang2.parser.core.Location;
import com.cowlang2.parser.core.ParseResult;

public class StatementParser extends Parser
{
	@Override
	protected ParseResult parseImpl(Location location)
	{
		ParseResult pr1 = ExpressionStatementParser.parse(location);
		if (pr1.success())
			return pr1;
		
		ParseResult pr2 = InterfaceDeclarationParser.parse(location);
		if (pr2.success())
			return pr2;
		
		ParseResult pr3 = VarDeclParser.parse(location);
		if (pr3.success())
			return pr3;
		
		return combineParseErrors(pr1, pr2, pr3);
	}
}
