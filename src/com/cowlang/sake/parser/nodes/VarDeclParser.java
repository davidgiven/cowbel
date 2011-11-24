package com.cowlang.sake.parser.nodes;

import com.cowlang.sake.parser.core.Location;
import com.cowlang.sake.parser.core.ParseResult;
import com.cowlang.sake.parser.tokens.ExpressionNode;
import com.cowlang.sake.parser.tokens.IdentifierNode;
import com.cowlang.sake.parser.tokens.StatementListNode;
import com.cowlang.sake.parser.tokens.VarAssignmentNode;
import com.cowlang.sake.parser.tokens.VarDeclarationNode;

public class VarDeclParser extends Parser
{
	@Override
	protected ParseResult parseImpl(Location location)
	{
		ParseResult pr = VarTokenParser.parse(location);
		if (pr.failed())
			return pr;
		
		ParseResult identifierpr = IdentifierParser.parse(pr.end());
		if (identifierpr.failed())
			return identifierpr;
			
		pr = EqualsParser.parse(identifierpr.end());
		if (pr.failed())
			return pr;
		
		ParseResult valuepr = ExpressionLowParser.parse(pr.end());
		if (valuepr.failed())
			return valuepr;
		
		pr = SemicolonParser.parse(valuepr.end());
		if (pr.failed())
			return pr;
		
		
		return new StatementListNode(location, pr.end(),
				new VarDeclarationNode(location, identifierpr.end(),
						(IdentifierNode) identifierpr),
				new VarAssignmentNode(identifierpr.start(), valuepr.end(),
						(IdentifierNode) identifierpr,
						(ExpressionNode) valuepr));
	}
}
