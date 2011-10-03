package com.cowlang2.parser.nodes;

import com.cowlang2.parser.core.Location;
import com.cowlang2.parser.core.ParseResult;
import com.cowlang2.parser.errors.InvalidStatement;
import com.cowlang2.parser.errors.UnimplementedParse;

public class InterfaceDeclarationParser extends Parser
{
	@Override
	protected ParseResult parseImpl(Location location)
	{
		ParseResult pr1 = InterfaceTokenParser.parse(location);
		if (pr1.failed())
			return pr1;
		
		ParseResult identifierpr = IdentifierParser.parse(pr1.end());
		if (identifierpr.failed())
			return identifierpr;
		
		Location n = identifierpr.end();
		ParseResult typexpr = TypexDeclarationParser.parse(n);
		if (typexpr.success())
			n = typexpr.end();
		
		ParseResult pr2 = OpenBraceParser.parse(n);
		if (pr2.failed())
			return combineParseErrors(identifierpr, pr2);
		
		n = pr2.end();
		for (;;)
		{
			ParseResult pr3 = CloseBraceParser.parse(n);
			if (pr3.success())
				break;
			
			return new UnimplementedParse(n);
		}
		
		return new InvalidStatement(location);
	}
}
