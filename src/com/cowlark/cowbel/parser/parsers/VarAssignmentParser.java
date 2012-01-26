/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel.parser.parsers;

import com.cowlark.cowbel.ast.nodes.ExpressionNode;
import com.cowlark.cowbel.ast.nodes.IdentifierNode;
import com.cowlark.cowbel.ast.nodes.VarAssignmentNode;
import com.cowlark.cowbel.parser.core.Location;
import com.cowlark.cowbel.parser.core.ParseResult;

public class VarAssignmentParser extends Parser
{
	@Override
	protected ParseResult parseImpl(Location location)
	{
		ParseResult identifierpr = IdentifierParser.parse(location);
		if (identifierpr.failed())
			return identifierpr;

		ParseResult pr = EqualsParser.parse(identifierpr.end());
		if (pr.failed())
			return pr;
		
		ParseResult valuepr = ExpressionLowParser.parse(pr.end());
		if (valuepr.failed())
			return valuepr;
		
		pr = SemicolonParser.parse(valuepr.end());
		if (pr.failed())
			return pr;
		
		return new VarAssignmentNode(location, pr.end(),
				(IdentifierNode) identifierpr,
				(ExpressionNode) valuepr); 
	}
}
