package com.cowlang.sake.parser.nodes;

import com.cowlang.sake.parser.core.Location;
import com.cowlang.sake.parser.core.ParseResult;
import com.cowlang.sake.parser.errors.UnimplementedParse;

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
	
		ParseResult typepr = null;
		Location n = identifierpr.end();
		pr = ColonParser.parse(n);
		if (pr.success())
		{
			typepr = TypeReferenceParser.parse(n);
			if (typepr.failed())
				return typepr;
			
			n = typepr.end();
		}
		
		pr = EqualsParser.parse(n);
		if (pr.failed())
			return pr;
		
		ParseResult valuepr = Expression1Parser.parse(pr.end());
		if (valuepr.failed())
			return valuepr;
		
		pr = SemicolonParser.parse(valuepr.end());
		if (pr.failed())
			return pr;
		
		return new UnimplementedParse(location); 
	}
}
