/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING.BSD in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel.parser.parsers;

import java.util.ArrayList;
import com.cowlark.cowbel.ast.AbstractStatementNode;
import com.cowlark.cowbel.ast.FunctionScopeConstructorNode;
import com.cowlark.cowbel.ast.StatementListNode;
import com.cowlark.cowbel.parser.core.Location;
import com.cowlark.cowbel.parser.core.ParseResult;

public class ProgramParser extends Parser
{
	@Override
	protected ParseResult parseImpl(Location location)
	{
		ArrayList<AbstractStatementNode> statements = new ArrayList<AbstractStatementNode>();
		
		Location n = location;
		for (;;)
		{
			ParseResult pr1 = EOFParser.parse(n);
			if (pr1.success())
				break;
			
			ParseResult pr2 = StatementParser.parse(n);
			if (pr2.failed())
				return pr2;
			
			statements.add((AbstractStatementNode) pr2);
			n = pr2.end();
		}
		
		return new FunctionScopeConstructorNode(location, n,
				new StatementListNode(location, n, statements)); 
	}
}
