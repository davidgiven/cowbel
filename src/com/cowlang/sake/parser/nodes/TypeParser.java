package com.cowlang.sake.parser.nodes;

import com.cowlang.sake.parser.core.Location;
import com.cowlang.sake.parser.core.ParseResult;
import com.cowlang.sake.parser.tokens.StringListTypeNode;
import com.cowlang.sake.parser.tokens.StringTypeNode;

public class TypeParser extends Parser
{
	@Override
	protected ParseResult parseImpl(Location location)
	{
		ParseResult pr1 = StringTokenParser.parse(location);
		if (pr1.success())
			return new StringTypeNode(location, pr1.end());
		
		ParseResult pr2 = OpenSquareParser.parse(location);
		if (pr2.failed())
			return combineParseErrors(pr1, pr2);
		
		ParseResult pr3 = StringTokenParser.parse(pr2.end());
		if (pr3.failed())
			return combineParseErrors(pr1, pr3);
		
		ParseResult pr4 = CloseSquareParser.parse(location);
		if (pr4.failed())
			return combineParseErrors(pr1, pr4);
		
		return new StringListTypeNode(location, pr4.end());
	}
}
