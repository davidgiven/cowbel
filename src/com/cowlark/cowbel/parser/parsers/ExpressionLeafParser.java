/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING.BSD in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel.parser.parsers;

import com.cowlark.cowbel.ast.BlockExpressionNode;
import com.cowlark.cowbel.ast.BlockScopeConstructorNode;
import com.cowlark.cowbel.ast.BooleanConstantNode;
import com.cowlark.cowbel.ast.IdentifierNode;
import com.cowlark.cowbel.ast.VarReferenceNode;
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
		
		ParseResult pr5 = TrueTokenParser.parse(location);
		if (pr5.success())
			return new BooleanConstantNode(location, pr5.end(), true);
		
		ParseResult pr6 = FalseTokenParser.parse(location);
		if (pr6.success())
			return new BooleanConstantNode(location, pr6.end(), false);
		
		ParseResult pr7 = NumericConstantParser.parse(location);
		if (pr7.success())
			return pr7;
		
		ParseResult pr8 = ScopeConstructorParser.parse(location);
		if (pr8.success())
			return new BlockExpressionNode(location, pr8.end(),
					(BlockScopeConstructorNode) pr8);
		
		ParseResult pr9 = ExternExpressionParser.parse(location);
		if (pr9.success())
			return pr9;
		
		return combineParseErrors(pr1, pr2, pr3, pr5, pr6, pr7, pr8, pr9);
	}
}
