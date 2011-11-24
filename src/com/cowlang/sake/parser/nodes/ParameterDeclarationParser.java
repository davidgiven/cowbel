package com.cowlang.sake.parser.nodes;

import com.cowlang.sake.parser.core.Location;
import com.cowlang.sake.parser.core.ParseResult;
import com.cowlang.sake.parser.tokens.IdentifierNode;
import com.cowlang.sake.parser.tokens.ParameterDeclarationNode;
import com.cowlang.sake.parser.tokens.TypeNode;

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
		
		ParseResult typepr = TypeParser.parse(pr.end());
		if (typepr.failed())
			return typepr;
			
		return new ParameterDeclarationNode(location, typepr.end(), 
				(IdentifierNode) identifierpr, 
				(TypeNode) typepr); 
	}
}
