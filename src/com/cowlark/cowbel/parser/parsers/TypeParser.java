/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING.BSD in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel.parser.parsers;

import com.cowlark.cowbel.ast.nodes.IdentifierNode;
import com.cowlark.cowbel.ast.nodes.TypeVariableNode;
import com.cowlark.cowbel.parser.core.Location;
import com.cowlark.cowbel.parser.core.ParseResult;

public class TypeParser extends Parser
{
	@Override
	protected ParseResult parseImpl(Location location)
	{
		ParseResult interfacepr = InterfaceDeclarationParser.parse(location);
		if (interfacepr.success())
			return interfacepr;
		
		ParseResult identifierpr = IdentifierParser.parse(location);
		if (identifierpr.success())
			return new TypeVariableNode(identifierpr, identifierpr.end(), (IdentifierNode) identifierpr);
		
		return combineParseErrors(interfacepr, identifierpr);
	}
}
