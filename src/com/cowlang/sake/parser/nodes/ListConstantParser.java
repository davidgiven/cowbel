package com.cowlang.sake.parser.nodes;

import java.util.ArrayList;
import com.cowlang.sake.parser.core.Location;
import com.cowlang.sake.parser.core.ParseResult;
import com.cowlang.sake.parser.tokens.ListConstantNode;
import com.cowlang.sake.parser.tokens.StringConstantNode;

public class ListConstantParser extends Parser
{
	@Override
	protected ParseResult parseImpl(Location location)
	{
		ParseResult pr = OpenSquareParser.parse(location);
		if (pr.failed())
			return pr;
		
		ArrayList<StringConstantNode> params = new ArrayList<StringConstantNode>();
		Location n = pr.end();
		pr = CloseSquareParser.parse(n);
		if (pr.success())
			n = pr.end();
		else
		{
			for (;;)
			{
				pr = StringConstantParser.parse(n);
				if (pr.failed())
					return pr;
		
				params.add((StringConstantNode) pr);
				
				n = pr.end();
				pr = CloseSquareParser.parse(n);
				if (pr.success())
				{
					n = pr.end();
					break;
				}
				
				n = pr.end();
				pr = CommaParser.parse(n);
				if (pr.failed())
					return pr;
				
				n = pr.end();
				pr = CloseSquareParser.parse(n);
				if (pr.success())
				{
					n = pr.end();
					break;
				}
			}
		}
			
		return new ListConstantNode(location, n, params); 
	}
}
