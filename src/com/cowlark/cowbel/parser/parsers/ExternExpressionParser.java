/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING.BSD in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel.parser.parsers;

import com.cowlark.cowbel.ast.AbstractExpressionNode;
import com.cowlark.cowbel.ast.ExternExpressionNode;
import com.cowlark.cowbel.parser.core.Location;
import com.cowlark.cowbel.parser.core.ParseResult;

public class ExternExpressionParser extends Parser
{
	@Override
	protected ParseResult parseImpl(Location location)
	{
		ParseResult pr = ExternTokenParser.parse(location);
		if (pr.failed())
			return pr;
		
		ParseResult exprpr = ParenthesisedExpressionParser.parse(pr.end());
		if (exprpr.failed())
			return exprpr;
		
		return new ExternExpressionNode(location, exprpr.end(),
				(AbstractExpressionNode) exprpr);
	}
}
