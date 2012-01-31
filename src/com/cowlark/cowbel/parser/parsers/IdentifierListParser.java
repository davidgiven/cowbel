/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel.parser.parsers;

import java.util.LinkedList;
import com.cowlark.cowbel.ast.nodes.IdentifierListNode;
import com.cowlark.cowbel.ast.nodes.IdentifierNode;
import com.cowlark.cowbel.parser.core.Location;
import com.cowlark.cowbel.parser.core.ParseResult;
import com.cowlark.cowbel.parser.errors.ExpectedSyntacticElement;

public class IdentifierListParser extends Parser
{
	private ParseResult parseTerminator(Location location)
	{
		ParseResult pr = EqualsParser.parse(location);
		if (pr.success())
			return pr;
		
		pr = CloseAngleBracketParser.parse(location);
		if (pr.success())
			return pr;
		
		return new ExpectedSyntacticElement(location, "identifier list terminator");
	}
	
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
			
			/* An = or > means the end of the assignment list. */
			
			ParseResult pr = parseTerminator(identifierpr.end());
			if (pr.success())
			{
				n = identifierpr.end();
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
