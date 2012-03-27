/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING.BSD in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel.parser.parsers;

import java.util.List;
import java.util.Vector;
import org.apache.commons.lang3.text.StrLookup;
import org.apache.commons.lang3.text.StrSubstitutor;
import com.cowlark.cowbel.ast.ExternStatementNode;
import com.cowlark.cowbel.ast.IdentifierNode;
import com.cowlark.cowbel.ast.StringConstantNode;
import com.cowlark.cowbel.ast.VarReferenceNode;
import com.cowlark.cowbel.parser.core.Location;
import com.cowlark.cowbel.parser.core.ParseResult;

public class ExternStatementParser extends Parser
{
	@Override
	protected ParseResult parseImpl(Location location)
	{
		ParseResult pr = ExternTokenParser.parse(location);
		if (pr.failed())
			return pr;
		
		ParseResult stringpr = StringConstantParser.parse(pr.end());
		if (stringpr.failed())
			return stringpr;
		
		pr = SemicolonParser.parse(stringpr.end());
		if (pr.failed())
			return pr;

		final List<VarReferenceNode> vars = new Vector<VarReferenceNode>();
		
		StrLookup<String> lookerupper = new StrLookup<String>()
		{
			@Override
			public String lookup(String arg)
			{
				int number = vars.size();
				IdentifierNode idnode = IdentifierNode.createIdentifier(arg);
				VarReferenceNode varnode = new VarReferenceNode(
						idnode.start(), idnode.end(), idnode);
				vars.add(varnode);
				
				return String.valueOf(Character.toChars(number + 0x100000));
			}
		};

		StrSubstitutor sub = new StrSubstitutor(lookerupper, "${", "}", (char)0);
		String template = sub.replace(((StringConstantNode) stringpr).getValue());
		
		return new ExternStatementNode(location, pr.end(),
				template,
				vars);
	}
}
