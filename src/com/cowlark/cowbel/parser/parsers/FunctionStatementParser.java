package com.cowlark.cowbel.parser.parsers;

import com.cowlark.cowbel.ast.nodes.ScopeConstructorNode;
import com.cowlark.cowbel.parser.core.Location;
import com.cowlark.cowbel.parser.core.ParseResult;

public class FunctionStatementParser extends Parser
{
	@Override
	protected ParseResult parseImpl(Location location)
	{
		ParseResult pr1 = VarAssignmentParser.parse(location);
		if (pr1.success())
			return pr1;
		
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
			
			return (ScopeConstructorNode) pr7;
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
		
		ParseResult pr14 = ExpressionStatementParser.parse(location);
		if (pr14.success())
			return pr14;
		
		return combineParseErrors(pr1, pr2, pr3, pr4, pr5, pr6, pr7, pr8, pr9,
				pr10, pr11, pr12, pr13, pr14);
	}
}
