/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING.BSD in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel.parser.parsers;

import com.cowlark.cowbel.ast.nodes.ExpressionStatementNode;
import com.cowlark.cowbel.parser.core.Location;
import com.cowlark.cowbel.parser.core.ParseResult;

public class ExpressionStatementParser extends Parser
{
	@Override
	protected ParseResult parseImpl(Location location)
	{
		ParseResult pr1 = ExpressionLowParser.parse(location);
		if (pr1.failed())
			return pr1;
		
		ParseResult pr2 = SemicolonParser.parse(pr1.end());
		if (pr2.failed())
			return pr2;
		
		return new ExpressionStatementNode(location, pr2.end(), pr1);
	}
}
