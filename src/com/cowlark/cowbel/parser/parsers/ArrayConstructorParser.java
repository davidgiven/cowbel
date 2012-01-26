package com.cowlark.cowbel.parser.parsers;

import java.util.ArrayList;
import com.cowlark.cowbel.ast.nodes.ArrayConstructorNode;
import com.cowlark.cowbel.ast.nodes.ExpressionNode;
import com.cowlark.cowbel.parser.core.Location;
import com.cowlark.cowbel.parser.core.ParseResult;

public class ArrayConstructorParser extends Parser
{
	@Override
	protected ParseResult parseImpl(Location location)
	{
		ParseResult pr = OpenSquareParser.parse(location);
		if (pr.failed())
			return pr;
		
		ArrayList<ExpressionNode> params = new ArrayList<ExpressionNode>();
		Location n = pr.end();
		pr = CloseSquareParser.parse(n);
		if (pr.success())
			n = pr.end();
		else
		{
			for (;;)
			{
				pr = ExpressionLowParser.parse(n);
				if (pr.failed())
					return pr;
		
				params.add((ExpressionNode) pr);
				
				n = pr.end();
				pr = CloseSquareParser.parse(n);
				if (pr.success())
				{
					n = pr.end();
					break;
				}
				
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
			
		return new ArrayConstructorNode(location, n, params); 
	}
}