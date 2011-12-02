package com.cowlark.sake.parser.nodes;

import com.cowlark.sake.ast.nodes.FunctionDefinitionNode;
import com.cowlark.sake.ast.nodes.FunctionHeaderNode;
import com.cowlark.sake.ast.nodes.ScopeNode;
import com.cowlark.sake.ast.nodes.StatementNode;
import com.cowlark.sake.parser.core.Location;
import com.cowlark.sake.parser.core.ParseResult;

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
				new ScopeNode(bodypr.start(), bodypr.end(),
						(StatementNode) bodypr));
	}
}
