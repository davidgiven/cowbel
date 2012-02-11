/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING.BSD in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel.parser.parsers;

import java.util.ArrayList;
import com.cowlark.cowbel.ast.AbstractStatementNode;
import com.cowlark.cowbel.ast.BlockScopeConstructorNode;
import com.cowlark.cowbel.ast.StatementListNode;
import com.cowlark.cowbel.parser.core.Location;
import com.cowlark.cowbel.parser.core.ParseResult;

public class ScopeConstructorParser extends Parser
{
	@Override
	protected ParseResult parseImpl(Location location)
	{
		ParseResult pr = OpenBraceParser.parse(location);
		if (pr.failed())
			return pr;
		
		ArrayList<AbstractStatementNode> statements = new ArrayList<AbstractStatementNode>();
		Location n = pr.end();
		for (;;)
		{
			pr = CloseBraceParser.parse(n);
			if (pr.success())
			{
				n = pr.end();
				break;
			}
			
			pr = StatementParser.parse(n);
			if (pr.failed())
				return pr;
			
			statements.add((AbstractStatementNode) pr);
			n = pr.end();
		}
		
		return new BlockScopeConstructorNode(location, n,
				new StatementListNode(location, n, statements));
	}
}
