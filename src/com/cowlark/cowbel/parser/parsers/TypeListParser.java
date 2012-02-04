/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING.BSD in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel.parser.parsers;

import java.util.LinkedList;
import com.cowlark.cowbel.ast.nodes.TypeListNode;
import com.cowlark.cowbel.ast.nodes.TypeVariableNode;
import com.cowlark.cowbel.parser.core.Location;
import com.cowlark.cowbel.parser.core.ParseResult;

public class TypeListParser extends Parser
{
	@Override
	protected ParseResult parseImpl(Location location)
	{
		ParseResult pr = OpenAngleBracketParser.parse(location);
		if (pr.failed())
		{
			/* Empty list */
			return new TypeListNode(location, location);
		}
		
		LinkedList<TypeVariableNode> args = new LinkedList<TypeVariableNode>();		
		Location n = pr.end();
		for (;;)
		{
			ParseResult arg = TypeParser.parse(n);
			if (arg.failed())
				return arg;
			args.addLast((TypeVariableNode) arg);
			
			pr = CloseAngleBracketParser.parse(arg.end());
			if (pr.success())
			{
				n = pr.end();
				break;
			}
			
			pr = CommaParser.parse(arg.end());
			if (pr.failed())
				return pr;
			n = pr.end();
		}

		return new TypeListNode(location, n, args);
	}
}
