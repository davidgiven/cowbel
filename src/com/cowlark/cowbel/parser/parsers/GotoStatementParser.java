/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel.parser.parsers;

import com.cowlark.cowbel.ast.nodes.GotoStatementNode;
import com.cowlark.cowbel.ast.nodes.IdentifierNode;
import com.cowlark.cowbel.parser.core.Location;
import com.cowlark.cowbel.parser.core.ParseResult;

public class GotoStatementParser extends Parser
{
	@Override
	protected ParseResult parseImpl(Location location)
	{
		ParseResult pr = GotoTokenParser.parse(location);
		if (pr.failed())
			return pr;
		
		ParseResult identifierpr = IdentifierParser.parse(pr.end());
		if (identifierpr.failed())
			return identifierpr;
		
		pr = SemicolonParser.parse(identifierpr.end());
		if (pr.failed())
			return pr;

		return new GotoStatementNode(location, pr.end(),
				(IdentifierNode) identifierpr);
	}
}
