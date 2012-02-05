/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING.BSD in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel.parser.parsers;

import java.util.ArrayList;
import com.cowlark.cowbel.ast.nodes.FunctionHeaderNode;
import com.cowlark.cowbel.ast.nodes.InterfaceTypeNode;
import com.cowlark.cowbel.parser.core.Location;
import com.cowlark.cowbel.parser.core.ParseResult;

public class InterfaceDeclarationParser extends Parser
{
	@Override
	protected ParseResult parseImpl(Location location)
	{
		ParseResult pr = OpenBraceParser.parse(location);
		if (pr.failed())
			return pr;
		
		ArrayList<FunctionHeaderNode> statements = new ArrayList<FunctionHeaderNode>();
		Location n = pr.end();
		for (;;)
		{
			pr = CloseBraceParser.parse(n);
			if (pr.success())
			{
				n = pr.end();
				break;
			}
			
			pr = InterfaceEntryParser.parse(n);
			if (pr.failed())
				return pr;
			
			statements.add((FunctionHeaderNode) pr);
			n = pr.end();
		}
		
		return new InterfaceTypeNode(location, n, statements);
	}
}
