package com.cowlark.cowbel.parser.parsers;

import com.cowlark.cowbel.ast.nodes.ExpressionNode;
import com.cowlark.cowbel.ast.nodes.IdentifierNode;
import com.cowlark.cowbel.ast.nodes.InferredTypeNode;
import com.cowlark.cowbel.ast.nodes.StatementListNode;
import com.cowlark.cowbel.ast.nodes.TypeNode;
import com.cowlark.cowbel.ast.nodes.VarAssignmentNode;
import com.cowlark.cowbel.ast.nodes.VarDeclarationNode;
import com.cowlark.cowbel.parser.core.Location;
import com.cowlark.cowbel.parser.core.ParseResult;

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
			
		Location n = identifierpr.end();
		ParseResult colonpr = ColonParser.parse(n);
		ParseResult typepr = null;
		if (colonpr.success())
		{
			typepr = TypeParser.parse(colonpr.end());
			if (typepr.failed())
				return typepr;
			
			n = typepr.end();
		}
		
		pr = EqualsParser.parse(n);
		if (pr.failed())
			return pr;
		
		ParseResult valuepr = ExpressionLowParser.parse(pr.end());
		if (valuepr.failed())
			return valuepr;
		
		pr = SemicolonParser.parse(valuepr.end());
		if (pr.failed())
			return pr;
		
		if (typepr == null)
			typepr = new InferredTypeNode(identifierpr.start(), identifierpr.end());
		
		return new StatementListNode(location, pr.end(),
				new VarDeclarationNode(location, identifierpr.end(),
						(IdentifierNode) identifierpr, (TypeNode) typepr,
						(ExpressionNode) valuepr),
				new VarAssignmentNode(identifierpr.start(), valuepr.end(),
						(IdentifierNode) identifierpr,
						(ExpressionNode) valuepr));
	}
}
