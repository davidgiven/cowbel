/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel.parser.parsers;

import com.cowlark.cowbel.ast.nodes.DoWhileStatementNode;
import com.cowlark.cowbel.ast.nodes.ExpressionNode;
import com.cowlark.cowbel.ast.nodes.ScopeConstructorNode;
import com.cowlark.cowbel.parser.core.Location;
import com.cowlark.cowbel.parser.core.ParseResult;

public class DoWhileStatementParser extends Parser
{
	@Override
	protected ParseResult parseImpl(Location location)
	{
		ParseResult pr = DoTokenParser.parse(location);
		if (pr.failed())
			return pr;
		
		ParseResult bodypr = ScopeConstructorParser.parse(pr.end());
		if (bodypr.failed())
			return bodypr;
		
		pr = WhileTokenParser.parse(bodypr.end());
		if (pr.failed())
			return pr;
		
		ParseResult conditionalpr = ExpressionLowParser.parse(pr.end());
		if (conditionalpr.failed())
			return conditionalpr;
		
		pr = SemicolonParser.parse(conditionalpr.end());
		if (pr.failed())
			return pr;
		
		return new DoWhileStatementNode(location, pr.end(),
				(ScopeConstructorNode) bodypr,
				(ExpressionNode) conditionalpr);
	}
}
