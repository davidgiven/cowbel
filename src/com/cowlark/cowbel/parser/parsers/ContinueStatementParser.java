/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel.parser.parsers;

import com.cowlark.cowbel.ast.nodes.ContinueStatementNode;
import com.cowlark.cowbel.parser.core.Location;
import com.cowlark.cowbel.parser.core.ParseResult;

public class ContinueStatementParser extends Parser
{
	@Override
	protected ParseResult parseImpl(Location location)
	{
		ParseResult pr = ContinueTokenParser.parse(location);
		if (pr.failed())
			return pr;
		
		pr = SemicolonParser.parse(pr.end());
		if (pr.failed())
			return pr;

		return new ContinueStatementNode(location, pr.end());
	}
}
