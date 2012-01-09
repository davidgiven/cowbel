package com.cowlark.sake.parser.parsers;

import com.cowlark.sake.ast.nodes.DoWhileStatementNode;
import com.cowlark.sake.ast.nodes.ExpressionNode;
import com.cowlark.sake.ast.nodes.StatementNode;
import com.cowlark.sake.parser.core.Location;
import com.cowlark.sake.parser.core.ParseResult;

public class DoWhileStatementParser extends Parser
{
	@Override
	protected ParseResult parseImpl(Location location)
	{
		ParseResult pr = DoTokenParser.parse(location);
		if (pr.failed())
			return pr;
		
		ParseResult bodypr = FunctionStatementParser.parse(pr.end());
		if (bodypr.failed())
			return bodypr;
		
		pr = WhileTokenParser.parse(bodypr.end());
		if (pr.failed())
			return pr;
		
		ParseResult conditionalpr = ExpressionHighParser.parse(pr.end());
		if (conditionalpr.failed())
			return conditionalpr;
		
		pr = SemicolonParser.parse(conditionalpr.end());
		if (pr.failed())
			return pr;
		
		return new DoWhileStatementNode(location, pr.end(),
				(StatementNode) bodypr,
				(ExpressionNode) conditionalpr);
	}
}
