package com.cowlark.sake.parser.nodes;

import java.util.ArrayList;
import com.cowlark.sake.ast.nodes.StatementListNode;
import com.cowlark.sake.ast.nodes.StatementNode;
import com.cowlark.sake.parser.core.Location;
import com.cowlark.sake.parser.core.ParseResult;

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
			
			ParseResult pr2 = TopLevelStatementParser.parse(n);
			if (pr2.failed())
				return pr2;
			
			statements.add((StatementNode) pr2);
			n = pr2.end();
		}
		
		return new StatementListNode(location, n, statements); 
	}
}
