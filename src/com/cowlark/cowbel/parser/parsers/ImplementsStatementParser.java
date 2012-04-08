/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING.BSD in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel.parser.parsers;

import com.cowlark.cowbel.ast.AbstractTypeExpressionNode;
import com.cowlark.cowbel.ast.ImplementsStatementNode;
import com.cowlark.cowbel.parser.core.Location;
import com.cowlark.cowbel.parser.core.ParseResult;

public class ImplementsStatementParser extends Parser
{
	@Override
	protected ParseResult parseImpl(Location location)
	{
		ParseResult pr = ImplementsTokenParser.parse(location);
		if (pr.failed())
			return pr;
		
		boolean isextern = false;
		Location n;
		ParseResult externpr = ExternTokenParser.parse(pr.end());
		if (externpr.success())
		{
			n = externpr.end();
			isextern = true;
		}
		else
			n = pr.end();
		
		ParseResult typepr = TypeParser.parse(n);
		if (typepr.failed())
			return typepr;
		
		pr = SemicolonParser.parse(typepr.end());
		if (pr.failed())
			return pr;

		return new ImplementsStatementNode(location, pr.end(),
				(AbstractTypeExpressionNode) typepr, isextern);
	}
}
