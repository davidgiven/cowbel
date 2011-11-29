package com.cowlark.sake.parser.nodes;

import com.cowlark.sake.ast.nodes.IdentifierNode;
import com.cowlark.sake.ast.nodes.ParameterDeclarationNode;
import com.cowlark.sake.ast.nodes.TypeNode;
import com.cowlark.sake.parser.core.Location;
import com.cowlark.sake.parser.core.ParseResult;

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
