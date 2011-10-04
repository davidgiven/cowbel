package com.cowlang2.parser.nodes;

import com.cowlang2.parser.core.Location;
import com.cowlang2.parser.core.ParseResult;
import com.cowlang2.parser.tokens.IdentifierNode;
import com.cowlang2.parser.tokens.ParameterDeclarationNode;
import com.cowlang2.parser.tokens.TypeReferenceNode;

public class ParameterDeclarationParser extends Parser
{
	@Override
	protected ParseResult parseImpl(Location location)
	{
		ParseResult identifierpr = IdentifierParser.parse(location);
		if (identifierpr.failed())
			return identifierpr;
		
		ParseResult pr = ColonParser.parse(identifierpr.end());
		if (pr.failed())
			return pr;
		
		ParseResult typepr = TypeReferenceParser.parse(pr.end());
		if (typepr.failed())
			return typepr;
			
		return new ParameterDeclarationNode(location, typepr.end(), 
				(IdentifierNode) identifierpr, 
				(TypeReferenceNode) typepr); 
	}
}
