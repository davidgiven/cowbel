package com.cowlark.cowbel.parser.parsers;

import java.util.ArrayList;
import com.cowlark.cowbel.ast.nodes.ParameterDeclarationListNode;
import com.cowlark.cowbel.ast.nodes.ParameterDeclarationNode;
import com.cowlark.cowbel.parser.core.Location;
import com.cowlark.cowbel.parser.core.ParseResult;

public class ParameterDeclarationListParser extends Parser
{
	@Override
	protected ParseResult parseImpl(Location location)
	{
		ParseResult pr = OpenParenthesisParser.parse(location);
		if (pr.failed())
			return pr;
		
		ArrayList<ParameterDeclarationNode> params = new ArrayList<ParameterDeclarationNode>();
		Location n = pr.end();
		pr = CloseParenthesisParser.parse(n);
		if (pr.success())
			n = pr.end();
		else
		{
			for (;;)
			{
				pr = ParameterDeclarationParser.parse(n);
				if (pr.failed())
					return pr;
		
				params.add((ParameterDeclarationNode) pr);
				
				n = pr.end();
				pr = CloseParenthesisParser.parse(n);
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
			}
		}
			
		return new ParameterDeclarationListNode(location, n, params); 
	}
}
