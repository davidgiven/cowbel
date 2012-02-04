/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING.BSD in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel.parser.parsers;

import java.util.ArrayList;
import com.cowlark.cowbel.ast.nodes.IdentifierListNode;
import com.cowlark.cowbel.ast.nodes.IdentifierNode;
import com.cowlark.cowbel.parser.core.Location;
import com.cowlark.cowbel.parser.core.ParseResult;

public class TypeVariableListParser extends Parser
{
	@Override
	protected ParseResult parseImpl(Location location)
	{
		ParseResult pr = OpenAngleBracketParser.parse(location);
		if (pr.failed())
		{
			/* Empty list. */
			return new IdentifierListNode(location, location);
		}
		
		ArrayList<IdentifierNode> params = new ArrayList<IdentifierNode>();
		Location n = pr.end();
		pr = CloseAngleBracketParser.parse(n);
		if (pr.success())
			n = pr.end();
		else
		{
			for (;;)
			{
				pr = IdentifierParser.parse(n);
				if (pr.failed())
					return pr;
		
				params.add((IdentifierNode) pr);
				
				n = pr.end();
				pr = CloseAngleBracketParser.parse(n);
				if (pr.success())
				{
					n = pr.end();
					break;
				}
				
				n = pr.end();
				pr = CommaParser.parse(n);
				if (pr.failed())
					return pr;
				
				n = pr.end();
			}
		}
			
		return new IdentifierListNode(location, n, params); 
	}
}
