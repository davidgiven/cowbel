/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING.BSD in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel.parser.parsers;

import com.cowlark.cowbel.ast.ExpressionListNode;
import com.cowlark.cowbel.ast.IdentifierListNode;
import com.cowlark.cowbel.ast.Node;
import com.cowlark.cowbel.ast.VarAssignmentNode;
import com.cowlark.cowbel.parser.core.Location;
import com.cowlark.cowbel.parser.core.ParseResult;

public class VarAssignmentParser extends Parser
{
	ParseResult parseWithVariableList(IdentifierListNode variablespr,
			Location location)
	{
		ParseResult valuespr = ExpressionListParser.parse(location);
		if (valuespr.failed())
			return valuespr;
		
		ParseResult pr = SemicolonParser.parse(valuespr.end());
		if (pr.failed())
			return pr;
		
		return new VarAssignmentNode(location, pr.end(),
				(IdentifierListNode) variablespr,
				(ExpressionListNode) valuespr); 
	}
	
	@Override
	protected ParseResult parseImpl(Location location)
	{
		ParseResult variablespr = IdentifierListParser.parse(location);
		if (variablespr.failed())
			return variablespr;

		Location n = variablespr.end();
		if (((Node) variablespr).getNumberOfChildren() > 0)
		{
			ParseResult pr = EqualsParser.parse(variablespr.end());
			if (pr.failed())
				return pr;
			n = pr.end();
		}
		
		return parseWithVariableList((IdentifierListNode) variablespr, n);
	}
}
