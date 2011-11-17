package com.cowlang.sake.parser.nodes;

import com.cowlang.sake.parser.core.Location;
import com.cowlang.sake.parser.core.ParseResult;
import com.cowlang.sake.parser.tokens.BlockNode;
import com.cowlang.sake.parser.tokens.MethodDefinitionNode;
import com.cowlang.sake.parser.tokens.MethodHeaderNode;

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
