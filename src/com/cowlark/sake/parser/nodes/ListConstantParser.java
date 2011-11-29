package com.cowlark.sake.parser.nodes;

import java.util.ArrayList;
import com.cowlark.sake.ast.nodes.ListConstantNode;
import com.cowlark.sake.ast.nodes.StringConstantNode;
import com.cowlark.sake.parser.core.Location;
import com.cowlark.sake.parser.core.ParseResult;

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
