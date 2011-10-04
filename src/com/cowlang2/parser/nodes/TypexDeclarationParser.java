package com.cowlang2.parser.nodes;

import java.util.ArrayList;
import com.cowlang2.parser.core.Location;
import com.cowlang2.parser.core.ParseResult;
import com.cowlang2.parser.tokens.IdentifierNode;
import com.cowlang2.parser.tokens.TypeParameterDeclarationListNode;

public class TypexDeclarationParser extends Parser
{
	@Override
	protected ParseResult parseImpl(Location location)
	{
		ParseResult pr1 = OpenAngleBracketParser.parse(location);
		if (pr1.failed())
			return pr1;

		ArrayList<IdentifierNode> identifiers = new ArrayList<IdentifierNode>();
		
		Location n = pr1.end();
		ParseResult pr2 = CloseAngleBracketParser.parse(n);
		if (pr2.failed())
		{
			for (;;)
			{
				ParseResult identifierpr = IdentifierParser.parse(pr2.end());
				if (identifierpr.failed())
					return identifierpr;
				identifiers.add((IdentifierNode) identifierpr);
				n = identifierpr.end();
				
				ParseResult pr3 = CloseAngleBracketParser.parse(n);
				if (pr3.success())
				{
					n = pr3.end();
					break;
				}
				
				ParseResult pr4 = CommaParser.parse(n);
				if (pr4.failed())
					return pr4;
				
				n = pr4.end();
			}
		}
		else
			n = pr2.end(); 
				
		return new TypeParameterDeclarationListNode(location, n, identifiers);
	}
}
