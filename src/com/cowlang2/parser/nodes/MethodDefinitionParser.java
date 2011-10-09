package com.cowlang2.parser.nodes;

import com.cowlang2.parser.core.Location;
import com.cowlang2.parser.core.ParseResult;
import com.cowlang2.parser.tokens.BlockNode;
import com.cowlang2.parser.tokens.MethodDefinitionNode;
import com.cowlang2.parser.tokens.MethodHeaderNode;

public class MethodDefinitionParser extends Parser
{
	@Override
	protected ParseResult parseImpl(Location location)
	{
		ParseResult headerpr = MethodHeaderParser.parse(location);
		if (headerpr.failed())
			return headerpr;

		ParseResult bodypr = BlockParser.parse(headerpr.end());
		if (bodypr.failed())
			return bodypr;
		
		return new MethodDefinitionNode(location, bodypr.end(),
				(MethodHeaderNode) headerpr,
				(BlockNode) bodypr);
	}
}
