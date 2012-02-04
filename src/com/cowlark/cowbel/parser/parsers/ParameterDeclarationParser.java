/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING.BSD in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel.parser.parsers;

import com.cowlark.cowbel.ast.nodes.IdentifierNode;
import com.cowlark.cowbel.ast.nodes.ParameterDeclarationNode;
import com.cowlark.cowbel.ast.nodes.TypeVariableNode;
import com.cowlark.cowbel.parser.core.Location;
import com.cowlark.cowbel.parser.core.ParseResult;

public class ParameterDeclarationParser extends Parser
{
	@Override
	protected ParseResult parseImpl(Location location)
	{
		ParseResult identifierpr = IdentifierParser.parse(location);
		if (identifierpr.failed())
			return identifierpr;
		
		ParseResult pr = ColonParser.parse(identifierpr.end());
		if (pr.failed())
			return pr;
		
		ParseResult typepr = TypeParser.parse(pr.end());
		if (typepr.failed())
			return typepr;
			
		return new ParameterDeclarationNode(location, typepr.end(), 
				(IdentifierNode) identifierpr, 
				(TypeVariableNode) typepr); 
	}
}
