/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING.BSD in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel.parser.parsers;

import com.cowlark.cowbel.ast.StringConstantNode;
import com.cowlark.cowbel.ast.TypeExternNode;
import com.cowlark.cowbel.parser.core.Location;
import com.cowlark.cowbel.parser.core.ParseResult;

public class TypeExternStatementParser extends Parser
{
	@Override
	protected ParseResult parseImpl(Location location)
	{
		ParseResult pr = TypeTokenParser.parse(location);
		if (pr.failed())
			return pr;

		pr = ExternTokenParser.parse(pr.end());
		if (pr.failed())
			return pr;
		
		ParseResult stringpr = StringConstantParser.parse(pr.end());
		if (stringpr.failed())
			return stringpr;
		
		pr = SemicolonParser.parse(stringpr.end());
		if (pr.failed())
			return pr;
		
		return new TypeExternNode(location, pr.end(),
				(StringConstantNode) stringpr);
	}
}
