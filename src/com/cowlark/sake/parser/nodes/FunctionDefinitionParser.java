package com.cowlark.sake.parser.nodes;

import com.cowlark.sake.parser.core.Location;
import com.cowlark.sake.parser.core.ParseResult;
import com.cowlark.sake.parser.tokens.BlockNode;
import com.cowlark.sake.parser.tokens.FunctionDefinitionNode;
import com.cowlark.sake.parser.tokens.FunctionHeaderNode;

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
