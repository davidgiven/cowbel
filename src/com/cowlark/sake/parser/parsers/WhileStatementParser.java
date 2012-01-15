package com.cowlark.sake.parser.parsers;

import com.cowlark.sake.ast.nodes.ExpressionNode;
import com.cowlark.sake.ast.nodes.ScopeConstructorNode;
import com.cowlark.sake.ast.nodes.WhileStatementNode;
import com.cowlark.sake.parser.core.Location;
import com.cowlark.sake.parser.core.ParseResult;

public class WhileStatementParser extends Parser
{
	@Override
	protected ParseResult parseImpl(Location location)
	{
		ParseResult pr = WhileTokenParser.parse(location);
		if (pr.failed())
			return pr;
		
		ParseResult conditionalpr = ExpressionLowParser.parse(pr.end());
		if (conditionalpr.failed())
			return conditionalpr;
		
		ParseResult bodypr = ScopeConstructorParser.parse(conditionalpr.end());
		if (bodypr.failed())
			return bodypr;
		
		return new WhileStatementNode(location, bodypr.end(),
				(ExpressionNode) conditionalpr,
				(ScopeConstructorNode) bodypr);
	}
}
