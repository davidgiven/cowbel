/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING.BSD in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel.parser.parsers;

import java.util.ArrayList;
import com.cowlark.cowbel.ast.AbstractTypeNode;
import com.cowlark.cowbel.ast.IdentifierNode;
import com.cowlark.cowbel.ast.InferredTypeNode;
import com.cowlark.cowbel.ast.ParameterDeclarationListNode;
import com.cowlark.cowbel.ast.ParameterDeclarationNode;
import com.cowlark.cowbel.parser.core.Location;
import com.cowlark.cowbel.parser.core.ParseResult;

public class VariableDeclarationListParser extends Parser
{
	@Override
	protected ParseResult parseImpl(Location location)
	{
		ArrayList<ParameterDeclarationNode> params = new ArrayList<ParameterDeclarationNode>();
		
		Location n = location;
		for (;;)
		{
			ParseResult identifierpr = IdentifierParser.parse(n);
			if (identifierpr.failed())
				return identifierpr;
			
			ParseResult typepr;
			ParseResult pr = ColonParser.parse(identifierpr.end());
			if (pr.success())
			{
				typepr = TypeParser.parse(pr.end());
				if (typepr.failed())
					return typepr;
				n = typepr.end();
			}
			else
			{
				typepr = new InferredTypeNode(identifierpr.start(), identifierpr.end());
				n = identifierpr.end();
			}
			
			ParameterDeclarationNode pdn = new ParameterDeclarationNode(
					identifierpr.start(), typepr.end(),
					(IdentifierNode) identifierpr,
					(AbstractTypeNode) typepr);
			params.add(pdn);

			/* An equals token marks the end */
			
			pr = EqualsParser.parse(n);
			if (pr.success())
			{
				n = pr.end();
				break;
			}
			
			/* Otherwise, there must be a comma and more identifiers */
			
			pr = CommaParser.parse(n);
			if (pr.failed())
				return pr;
			
			n = pr.end();
		}
			
		return new ParameterDeclarationListNode(location, n, params); 
	}
}
