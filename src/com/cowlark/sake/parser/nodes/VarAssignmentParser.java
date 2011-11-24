package com.cowlark.sake.parser.nodes;

import com.cowlark.sake.parser.core.Location;
import com.cowlark.sake.parser.core.ParseResult;
import com.cowlark.sake.parser.tokens.ExpressionNode;
import com.cowlark.sake.parser.tokens.IdentifierNode;
import com.cowlark.sake.parser.tokens.VarAssignmentNode;

public class VarAssignmentParser extends Parser
{
	@Override
	protected ParseResult parseImpl(Location location)
	{
		ParseResult identifierpr = IdentifierParser.parse(location);
		if (identifierpr.failed())
			return identifierpr;

		ParseResult pr = EqualsParser.parse(identifierpr.end());
		if (pr.failed())
			return pr;
		
		ParseResult valuepr = ExpressionLowParser.parse(pr.end());
		if (valuepr.failed())
			return valuepr;
		
		pr = SemicolonParser.parse(valuepr.end());
		if (pr.failed())
			return pr;
		
		return new VarAssignmentNode(location, pr.end(),
				(IdentifierNode) identifierpr,
				(ExpressionNode) valuepr); 
	}
}
