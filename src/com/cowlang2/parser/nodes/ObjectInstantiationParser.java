package com.cowlang2.parser.nodes;

import java.util.ArrayList;
import com.cowlang2.parser.core.Location;
import com.cowlang2.parser.core.ParseResult;
import com.cowlang2.parser.errors.UnimplementedParse;
import com.cowlang2.parser.tokens.MethodDefinitionNode;

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
		
		ArrayList<MethodDefinitionNode> methods = new ArrayList<MethodDefinitionNode>();
		Location n = pr.end();
		for (;;)
		{
			pr = CloseBraceParser.parse(n);
			if (pr.success())
				break;
			
			pr = MethodDefinitionParser.parse(n);
			if (pr.success())
			{
				n = pr.end();
				continue;
			}

			return new UnimplementedParse(location);
		}
		
		return new UnimplementedParse(location);
	}
}
