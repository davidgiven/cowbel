package com.cowlang2.parser.nodes;

import java.util.ArrayList;
import com.cowlang2.parser.core.Location;
import com.cowlang2.parser.core.ParseResult;
import com.cowlang2.parser.tokens.IdentifierNode;
import com.cowlang2.parser.tokens.MethodDeclarationNode;
import com.cowlang2.parser.tokens.ParameterDeclarationListNode;
import com.cowlang2.parser.tokens.TypeParameterDeclarationListNode;

public class MethodDeclarationParser extends Parser
{
	@Override
	protected ParseResult parseImpl(Location location)
	{
		ParseResult pr = MethodTokenParser.parse(location);
		if (pr.failed())
			return pr;
		
		ParseResult namepr = MethodNameParser.parse(pr.end());
		if (namepr.failed())
			return namepr;
		
		Location n = namepr.end();
		ParseResult typexpr = TypexDeclarationParser.parse(n);
		if (typexpr.success())
			n = typexpr.end();
		else
			typexpr = new TypeParameterDeclarationListNode(n, n,
					new ArrayList<IdentifierNode>());
		
		ParseResult inputargspr = ParameterDeclarationListParser.parse(n);
		if (inputargspr.failed())
			return inputargspr;
		
		pr = ColonParser.parse(inputargspr.end());
		if (pr.failed())
			return pr;
		
		ParseResult outputargspr = ParameterDeclarationListParser.parse(pr.end());
		if (outputargspr.failed())
			return outputargspr;
		
		pr = SemicolonParser.parse(outputargspr.end());
		if (pr.failed())
			return pr;
		
		return new MethodDeclarationNode(location, pr.end(),
				(IdentifierNode) namepr, (TypeParameterDeclarationListNode) typexpr,
				(ParameterDeclarationListNode) inputargspr,
				(ParameterDeclarationListNode) outputargspr);
	}
}
