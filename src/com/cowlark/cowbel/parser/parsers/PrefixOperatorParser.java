/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel.parser.parsers;

import com.cowlark.cowbel.ast.nodes.ArgumentListNode;
import com.cowlark.cowbel.ast.nodes.ExpressionNode;
import com.cowlark.cowbel.ast.nodes.IdentifierNode;
import com.cowlark.cowbel.ast.nodes.MethodCallExpressionNode;
import com.cowlark.cowbel.parser.core.Location;
import com.cowlark.cowbel.parser.core.ParseResult;

public class PrefixOperatorParser extends Parser
{
	@Override
	protected ParseResult parseImpl(Location location)
	{
		ParseResult pr1 = OperatorParser.parse(location);
		if (pr1.failed())
			return pr1;
		
		ParseResult pr2 = ExpressionMediumParser.parse(pr1.end());
		if (pr2.failed())
			return pr2;
		
		return new MethodCallExpressionNode(location, pr2.end(),
				(ExpressionNode)pr2, (IdentifierNode)pr1,
				new ArgumentListNode(pr1, pr1.end()));
	}
}
