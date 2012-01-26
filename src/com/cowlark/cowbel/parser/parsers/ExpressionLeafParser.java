/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel.parser.parsers;

import com.cowlark.cowbel.ast.nodes.BooleanConstantNode;
import com.cowlark.cowbel.ast.nodes.IdentifierNode;
import com.cowlark.cowbel.ast.nodes.VarReferenceNode;
import com.cowlark.cowbel.parser.core.Location;
import com.cowlark.cowbel.parser.core.ParseResult;

public class ExpressionLeafParser extends Parser
{
	@Override
	protected ParseResult parseImpl(Location location)
	{
		ParseResult pr1 = ParenthesisedExpressionParser.parse(location);
		if (pr1.success())
			return pr1;
		
		ParseResult pr2 = IdentifierParser.parse(location);
		if (pr2.success())
			return new VarReferenceNode(pr2.start(), pr2.end(),
					(IdentifierNode) pr2);
		
		ParseResult pr3 = StringConstantParser.parse(location);
		if (pr3.success())
			return pr3;
		
		ParseResult pr4 = ArrayConstructorParser.parse(location);
		if (pr4.success())
			return pr4;
		
		ParseResult pr5 = TrueTokenParser.parse(location);
		if (pr5.success())
			return new BooleanConstantNode(location, pr5.end(), true);
		
		ParseResult pr6 = FalseTokenParser.parse(location);
		if (pr6.success())
			return new BooleanConstantNode(location, pr6.end(), false);
		
		ParseResult pr7 = IntegerConstantParser.parse(location);
		if (pr7.success())
			return pr7;
		
		return combineParseErrors(pr1, pr2, pr3, pr4, pr5, pr6, pr7);
	}
}
