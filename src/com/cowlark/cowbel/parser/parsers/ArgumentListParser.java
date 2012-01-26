/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel.parser.parsers;

import java.util.LinkedList;
import com.cowlark.cowbel.ast.nodes.ArgumentListNode;
import com.cowlark.cowbel.ast.nodes.ExpressionNode;
import com.cowlark.cowbel.parser.core.Location;
import com.cowlark.cowbel.parser.core.ParseResult;

public class ArgumentListParser extends Parser
{
	@Override
	protected ParseResult parseImpl(Location location)
	{
		LinkedList<ExpressionNode> args = new LinkedList<ExpressionNode>();
		
		ParseResult pr = OpenParenthesisParser.parse(location);
		if (pr.failed())
			return pr;
		
		pr = CloseParenthesisParser.parse(pr.end());
		if (pr.failed())
		{
			/* Argument list is not empty. */
			
			for (;;)
			{
				ParseResult arg = ExpressionLowParser.parse(pr.end());
				if (arg.failed())
					return arg;
				args.addLast((ExpressionNode)arg);
				
				pr = CloseParenthesisParser.parse(arg.end());
				if (pr.success())
					break;
				
				pr = CommaParser.parse(arg.end());
				if (pr.failed())
					return pr;
			}
		}

		return new ArgumentListNode(location, pr.end(), args);
	}
}
