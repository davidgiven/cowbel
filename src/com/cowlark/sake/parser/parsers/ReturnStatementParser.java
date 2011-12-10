package com.cowlark.sake.parser.parsers;

import com.cowlark.sake.ast.nodes.ExpressionNode;
import com.cowlark.sake.ast.nodes.ReturnStatementNode;
import com.cowlark.sake.ast.nodes.ReturnVoidStatementNode;
import com.cowlark.sake.parser.core.Location;
import com.cowlark.sake.parser.core.ParseResult;

public class ReturnStatementParser extends Parser
{
	@Override
	protected ParseResult parseImpl(Location location)
	{
		ParseResult pr = ReturnTokenParser.parse(location);
		if (pr.failed())
			return pr;
		
		ParseResult semipr = SemicolonParser.parse(pr.end());
		if (semipr.success())
			return new ReturnVoidStatementNode(location, semipr.end());
		
		ParseResult valuepr = ExpressionLowParser.parse(pr.end());
		if (valuepr.failed())
			return valuepr;
		
		pr = SemicolonParser.parse(valuepr.end());
		if (pr.failed())
			return pr;

		return new ReturnStatementNode(location, pr.end(),
				(ExpressionNode) valuepr);
	}
}
