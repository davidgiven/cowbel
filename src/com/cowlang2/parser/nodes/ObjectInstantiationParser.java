package com.cowlang2.parser.nodes;

import java.util.ArrayList;
import com.cowlang2.parser.core.Location;
import com.cowlang2.parser.core.ParseResult;
import com.cowlang2.parser.errors.UnimplementedParse;
import com.cowlang2.parser.tokens.MethodDefinitionNode;
import com.cowlang2.parser.tokens.ObjectInstantiationMemberNode;
import com.cowlang2.parser.tokens.ObjectInstantiationNode;

public class ObjectInstantiationParser extends Parser
{
	@Override
	protected ParseResult parseImpl(Location location)
	{
		ParseResult pr = NewTokenParser.parse(location);
		if (pr.failed())
			return pr;
		
		pr = OpenBraceParser.parse(pr.end());
		if (pr.failed())
			return pr;
		
		ArrayList<ObjectInstantiationMemberNode> methods = new ArrayList<ObjectInstantiationMemberNode>();
		Location n = pr.end();
		for (;;)
		{
			pr = CloseBraceParser.parse(n);
			if (pr.success())
			{
				n = pr.end();
				break;
			}
			
			pr = MethodDefinitionParser.parse(n);
			if (pr.success())
			{
				n = pr.end();
				methods.add((MethodDefinitionNode) pr);
				continue;
			}

			return new UnimplementedParse(location);
		}
		
		return new ObjectInstantiationNode(location, n, methods);
	}
}
