/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING.BSD in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel.parser.parsers;

import com.cowlark.cowbel.ast.AbstractExpressionNode;
import com.cowlark.cowbel.ast.DummyExpressionNode;
import com.cowlark.cowbel.parser.core.Location;
import com.cowlark.cowbel.parser.core.ParseResult;

public class ParenthesisedExpressionParser extends Parser
{
	@Override
	protected ParseResult parseImpl(Location location)
	{
		ParseResult pr1 = OpenParenthesisParser.parse(location);
		if (pr1.failed())
			return pr1;
		
		ParseResult pr2 = ExpressionLowParser.parse(pr1.end());
		if (pr2.failed())
			return pr2;
		
		ParseResult pr3 = CloseParenthesisParser.parse(pr2.end());
		if (pr3.failed())
			return pr3;

		return new DummyExpressionNode(pr1.start(), pr3.end(), (AbstractExpressionNode)pr2);
	}
}
