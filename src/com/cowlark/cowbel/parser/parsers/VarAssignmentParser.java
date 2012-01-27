/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel.parser.parsers;

import com.cowlark.cowbel.ast.nodes.ExpressionNode;
import com.cowlark.cowbel.ast.nodes.IdentifierListNode;
import com.cowlark.cowbel.ast.nodes.VarAssignmentNode;
import com.cowlark.cowbel.parser.core.Location;
import com.cowlark.cowbel.parser.core.ParseResult;

public class VarAssignmentParser extends Parser
{
	ParseResult parseWithVariableList(IdentifierListNode variablespr,
			Location location)
	{
		ParseResult valuepr = ExpressionLowParser.parse(location);
		if (valuepr.failed())
			return valuepr;
		
		ParseResult pr = SemicolonParser.parse(valuepr.end());
		if (pr.failed())
			return pr;
		
		return new VarAssignmentNode(location, pr.end(),
				(IdentifierListNode) variablespr,
				(ExpressionNode) valuepr); 
	}
	
	@Override
	protected ParseResult parseImpl(Location location)
	{
		ParseResult variablespr = IdentifierListParser.parse(location);
		if (variablespr.failed())
			return variablespr;

		return parseWithVariableList((IdentifierListNode) variablespr,
				variablespr.end());
	}
}
