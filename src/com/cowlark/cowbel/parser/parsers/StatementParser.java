/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING.BSD in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel.parser.parsers;

import com.cowlark.cowbel.ast.nodes.AbstractScopeConstructorNode;
import com.cowlark.cowbel.parser.core.Location;
import com.cowlark.cowbel.parser.core.ParseResult;

public class StatementParser extends Parser
{
	@Override
	protected ParseResult parseImpl(Location location)
	{
		ParseResult pr2 = VarDeclParser.parse(location);
		if (pr2.success())
			return pr2;
		
		ParseResult pr3 = ReturnStatementParser.parse(location);
		if (pr3.success())
			return pr3;
		
		ParseResult pr4 = IfStatementParser.parse(location);
		if (pr4.success())
			return pr4;
		
		ParseResult pr5 = LabelStatementParser.parse(location);
		if (pr5.success())
			return pr5;
		
		ParseResult pr6 = GotoStatementParser.parse(location);
		if (pr6.success())
			return pr6;
		
		ParseResult pr7 = OpenBraceParser.parse(location);
		if (pr7.success())
		{
			pr7 = ScopeConstructorParser.parse(location);
			if (pr7.failed())
				return pr7;
			
			return (AbstractScopeConstructorNode) pr7;
		}
		
		ParseResult pr8 = FunctionDefinitionParser.parse(location);
		if (pr8.success())
			return pr8;
		
		ParseResult pr9 = WhileStatementParser.parse(location);
		if (pr9.success())
			return pr9;
		
		ParseResult pr10 = BreakStatementParser.parse(location);
		if (pr10.success())
			return pr10;
		
		ParseResult pr11 = ContinueStatementParser.parse(location);
		if (pr11.success())
			return pr11;
		
		ParseResult pr12 = DoWhileStatementParser.parse(location);
		if (pr12.success())
			return pr12;
		
		ParseResult pr13 = ForStatementParser.parse(location);
		if (pr13.success())
			return pr13;
		
		/* The order of these is important. */
		
		ParseResult pr14 = DirectFunctionCallStatementParser.parse(location);
		if (pr14.success())
			return pr14;
		
		ParseResult pr15 = MethodCallStatementParser.parse(location);
		if (pr15.success())
			return pr15;
		
		ParseResult pr1 = VarAssignmentParser.parse(location);
		if (pr1.success())
			return pr1;
		
		ParseResult pr16 = ExpressionStatementParser.parse(location);
		if (pr16.success())
			return pr16;
		
		return combineParseErrors(pr1, pr2, pr3, pr4, pr5, pr6, pr7, pr8, pr9,
				pr10, pr11, pr12, pr13, pr14, pr15, pr16);
	}
}
