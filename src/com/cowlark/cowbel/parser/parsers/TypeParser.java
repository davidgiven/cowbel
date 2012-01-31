/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel.parser.parsers;

import com.cowlark.cowbel.ast.nodes.ArrayTypeNode;
import com.cowlark.cowbel.ast.nodes.IdentifierNode;
import com.cowlark.cowbel.ast.nodes.TypeNode;
import com.cowlark.cowbel.ast.nodes.TypeVariableNode;
import com.cowlark.cowbel.parser.core.Location;
import com.cowlark.cowbel.parser.core.ParseResult;

public class TypeParser extends Parser
{
	@Override
	protected ParseResult parseImpl(Location location)
	{
		ParseResult pr0 = IdentifierParser.parse(location);
		if (pr0.success())
			return new TypeVariableNode(pr0, pr0.end(),
					(IdentifierNode) pr0);
		
		ParseResult pr2 = OpenSquareParser.parse(location);
		if (pr2.failed())
			return combineParseErrors(pr0, pr2);
		
		ParseResult childpr = TypeParser.parse(pr2.end());
		if (childpr.failed())
			return combineParseErrors(pr0, childpr);
		
		ParseResult pr4 = CloseSquareParser.parse(childpr.end());
		if (pr4.failed())
			return combineParseErrors(pr0, pr4);
		
		return new ArrayTypeNode(location, pr4.end(), (TypeNode) childpr);
	}
}
