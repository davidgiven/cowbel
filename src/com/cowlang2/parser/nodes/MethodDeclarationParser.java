package com.cowlang2.parser.nodes;

import com.cowlang2.parser.core.Location;
import com.cowlang2.parser.core.ParseResult;
import com.cowlang2.parser.tokens.MethodDeclarationNode;
import com.cowlang2.parser.tokens.MethodHeaderNode;

public class MethodDeclarationParser extends Parser
{
	@Override
	protected ParseResult parseImpl(Location location)
	{
		ParseResult headerpr = MethodHeaderParser.parse(location);
		if (headerpr.failed())
			return headerpr;
		
		ParseResult pr = SemicolonParser.parse(headerpr.end());
		if (pr.failed())
			return pr;
		
		return new MethodDeclarationNode(location, pr.end(),
				(MethodHeaderNode) headerpr);
	}
}
