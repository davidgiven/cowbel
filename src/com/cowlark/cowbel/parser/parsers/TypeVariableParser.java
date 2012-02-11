/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING.BSD in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel.parser.parsers;

import com.cowlark.cowbel.ast.IdentifierNode;
import com.cowlark.cowbel.ast.TypeListNode;
import com.cowlark.cowbel.ast.TypeVariableNode;
import com.cowlark.cowbel.parser.core.Location;
import com.cowlark.cowbel.parser.core.ParseResult;

public class TypeVariableParser extends Parser
{
	@Override
	protected ParseResult parseImpl(Location location)
	{
		ParseResult identifierpr = IdentifierParser.parse(location);
		if (identifierpr.failed())
			return identifierpr;
		
		ParseResult typeassignmentspr = TypeListParser.parse(identifierpr.end());
		if (typeassignmentspr.failed())
			return typeassignmentspr;

		return new TypeVariableNode(location, typeassignmentspr.end(),
				(IdentifierNode) identifierpr,
				(TypeListNode) typeassignmentspr);
	}
}
