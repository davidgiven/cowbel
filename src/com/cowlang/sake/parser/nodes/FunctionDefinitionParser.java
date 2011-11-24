package com.cowlang.sake.parser.nodes;

import com.cowlang.sake.parser.core.Location;
import com.cowlang.sake.parser.core.ParseResult;
import com.cowlang.sake.parser.tokens.BlockNode;
import com.cowlang.sake.parser.tokens.FunctionDefinitionNode;
import com.cowlang.sake.parser.tokens.FunctionHeaderNode;

public class FunctionDefinitionParser extends Parser
{
	@Override
	protected ParseResult parseImpl(Location location)
	{
		ParseResult headerpr = FunctionHeaderParser.parse(location);
		if (headerpr.failed())
			return headerpr;

		ParseResult bodypr = BlockParser.parse(headerpr.end());
		if (bodypr.failed())
			return bodypr;
		
		return new FunctionDefinitionNode(location, bodypr.end(),
				(FunctionHeaderNode) headerpr,
				(BlockNode) bodypr);
	}
}
