package com.cowlark.cowbel.parser.parsers;

import java.util.ArrayList;
import com.cowlark.cowbel.ast.nodes.FunctionScopeConstructorNode;
import com.cowlark.cowbel.ast.nodes.StatementListNode;
import com.cowlark.cowbel.ast.nodes.StatementNode;
import com.cowlark.cowbel.parser.core.Location;
import com.cowlark.cowbel.parser.core.ParseResult;

public class ProgramParser extends Parser
{
	@Override
	protected ParseResult parseImpl(Location location)
	{
		ArrayList<StatementNode> statements = new ArrayList<StatementNode>();
		
		Location n = location;
		for (;;)
		{
			ParseResult pr1 = EOFParser.parse(n);
			if (pr1.success())
				break;
			
			ParseResult pr2 = FunctionStatementParser.parse(n);
			if (pr2.failed())
				return pr2;
			
			statements.add((StatementNode) pr2);
			n = pr2.end();
		}
		
		return new FunctionScopeConstructorNode(location, n,
				new StatementListNode(location, n, statements)); 
	}
}
