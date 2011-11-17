package com.cowlang.sake.parser.nodes;

import java.util.ArrayList;
import com.cowlang.sake.parser.core.Location;
import com.cowlang.sake.parser.core.ParseResult;
import com.cowlang.sake.parser.tokens.IdentifierNode;
import com.cowlang.sake.parser.tokens.TextToken;
import com.cowlang.sake.parser.tokens.TypeReferenceNode;

public class TypeReferenceParser extends Parser
{
	@Override
	protected ParseResult parseImpl(Location location)
	{
		ParseResult identifierpr = IdentifierParser.parse(location);
		if (identifierpr.failed())
			return identifierpr;
		
		ArrayList<TypeReferenceNode> typeparms = new ArrayList<TypeReferenceNode>();
		Location n = identifierpr.end();

		ParseResult pr = OpenAngleBracketParser.parse(n);
		if (pr.success())
		{
			n = pr.end();
			pr = CloseAngleBracketParser.parse(n);
			if (pr.success())
				n = pr.end();
			else
			{
				for (;;)
				{
					pr = TypeReferenceParser.parse(n);
					if (pr.failed())
						return pr;
					
					typeparms.add((TypeReferenceNode) pr);
					n = pr.end();
					
					pr = CloseAngleBracketParser.parse(n);
					if (pr.success())
					{
						n = pr.end();
						break;
					}
					
					pr = CommaParser.parse(n);
					if (pr.failed())
						return pr;
					
					n = pr.end();
				}
			}
		}

		return new TypeReferenceNode(location, n, (IdentifierNode)identifierpr,
				typeparms);
	}
}
