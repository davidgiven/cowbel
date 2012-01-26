package com.cowlark.cowbel.parser.parsers;

import java.util.LinkedList;
import com.cowlark.cowbel.ast.nodes.IdentifierListNode;
import com.cowlark.cowbel.ast.nodes.IdentifierNode;
import com.cowlark.cowbel.parser.core.Location;
import com.cowlark.cowbel.parser.core.ParseResult;

public class IdentifierListParser extends Parser
{
	@Override
	protected ParseResult parseImpl(Location location)
	{
		LinkedList<IdentifierNode> ids = new LinkedList<IdentifierNode>();
		
		Location n = location;
		for (;;)
		{
			ParseResult identifierpr = IdentifierParser.parse(n);
			if (identifierpr.failed())
				return identifierpr;
			
			ids.add((IdentifierNode) identifierpr);
			
			/* An = means the end of the assignment list. */
			
			ParseResult pr = EqualsParser.parse(identifierpr.end());
			if (pr.success())
			{
				n = pr.end();
				break;
			}
			
			/* Next token must be a separator comma. */
			
			pr = CommaParser.parse(identifierpr.end());
			if (pr.failed())
				return pr;
			
			n = pr.end();
		}
		
		return new IdentifierListNode(location, n, ids);
	}
}
