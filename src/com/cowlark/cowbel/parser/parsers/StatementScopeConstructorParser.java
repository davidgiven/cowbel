/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING.BSD in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel.parser.parsers;

import com.cowlark.cowbel.ast.AbstractStatementNode;
import com.cowlark.cowbel.ast.BlockScopeConstructorNode;
import com.cowlark.cowbel.parser.core.Location;
import com.cowlark.cowbel.parser.core.ParseResult;

public class StatementScopeConstructorParser extends Parser
{
	@Override
	protected ParseResult parseImpl(Location location)
	{
		ParseResult pr = OpenBraceParser.parse(location);
		if (pr.failed())
		{
			/* This is a single-statement block. */
			
			pr = StatementParser.parse(location);
			if (pr.failed())
				return pr;
			
			return new BlockScopeConstructorNode(location, pr.end(),
					(AbstractStatementNode) pr);
		}
		
		return ScopeConstructorParser.parse(location);
	}
}
