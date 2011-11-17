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
			ParseResult pr = EOFParser.parse(n);
			if (pr.success())
				break;
			
			pr = StatementParser.parse(n);
			if (pr.failed())
				return pr;
			
			statements.add((StatementNode) pr);
			n = pr.end();
		}
		
		return new StatementListNode(location, n, statements); 
	}
}
