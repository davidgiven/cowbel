/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel.parser.parsers;

import java.util.LinkedList;
import com.cowlark.cowbel.ast.nodes.ExpressionListNode;
import com.cowlark.cowbel.ast.nodes.ExpressionNode;
import com.cowlark.cowbel.parser.core.Location;
import com.cowlark.cowbel.parser.core.ParseResult;
import com.cowlark.cowbel.parser.errors.ExpectedSyntacticElement;

public class ExpressionListParser extends Parser
{
	private ParseResult parseTerminator(Location location)
	{
		ParseResult pr = SemicolonParser.parse(location);
		if (pr.success())
			return pr;
		
		pr = CloseParenthesisParser.parse(location);
		if (pr.success())
			return pr;
		
		return new ExpectedSyntacticElement(location, "list terminator");
	}
	
	@Override
	protected ParseResult parseImpl(Location location)
	{
		LinkedList<ExpressionNode> args = new LinkedList<ExpressionNode>();
		
		Location n = location;
		ParseResult pr = parseTerminator(location);
		if (pr.failed())
		{
			/* Non-empty list. */
			
			n = pr.end(); 
			for (;;)
			{
				ParseResult arg = ExpressionLowParser.parse(n);
				if (arg.failed())
					return arg;
				args.addLast((ExpressionNode)arg);
				
				pr = parseTerminator(arg.end());
				if (pr.success())
				{
					/* Don't consume the terminator (the parent will do
					 * that). */
					n = arg.end();
					break;
				}
				
				pr = CommaParser.parse(arg.end());
				if (pr.failed())
					return pr;
				n = pr.end();
			}
		}

		return new ExpressionListNode(location, n, args);
	}
}
