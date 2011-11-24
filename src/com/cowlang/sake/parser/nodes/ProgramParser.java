package com.cowlang.sake.parser.nodes;

import java.util.ArrayList;
import com.cowlang.sake.parser.core.Location;
import com.cowlang.sake.parser.core.ParseResult;
import com.cowlang.sake.parser.tokens.StatementListNode;
import com.cowlang.sake.parser.tokens.StatementNode;

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
