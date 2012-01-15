package com.cowlark.sake.parser.parsers;

import java.util.ArrayList;
import com.cowlark.sake.ast.nodes.ScopeConstructorNode;
import com.cowlark.sake.ast.nodes.StatementListNode;
import com.cowlark.sake.ast.nodes.StatementNode;
import com.cowlark.sake.parser.core.Location;
import com.cowlark.sake.parser.core.ParseResult;

public class ScopeConstructorParser extends Parser
{
	@Override
	protected ParseResult parseImpl(Location location)
	{
		ParseResult pr = OpenBraceParser.parse(location);
		if (pr.failed())
		{
			/* This is a single-statement block. */
			
			pr = FunctionStatementParser.parse(location);
			if (pr.failed())
				return pr;
			
			return new ScopeConstructorNode(location, pr.end(),
					(StatementNode) pr);
		}
		
		ArrayList<StatementNode> statements = new ArrayList<StatementNode>();
		Location n = pr.end();
		for (;;)
		{
			pr = CloseBraceParser.parse(n);
			if (pr.success())
			{
				n = pr.end();
				break;
			}
			
			pr = FunctionStatementParser.parse(n);
			if (pr.failed())
				return pr;
			
			statements.add((StatementNode) pr);
			n = pr.end();
		}
		
		return new ScopeConstructorNode(location, n,
				new StatementListNode(location, n, statements));
	}
}
