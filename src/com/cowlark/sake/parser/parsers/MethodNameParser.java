package com.cowlark.sake.parser.parsers;

import com.cowlark.sake.ast.nodes.IdentifierNode;
import com.cowlark.sake.parser.core.Location;
import com.cowlark.sake.parser.core.MutableLocation;
import com.cowlark.sake.parser.core.ParseResult;

public class MethodNameParser extends Parser
{
	@Override
	protected ParseResult parseImpl(Location location)
	{
		ParseResult pr1 = OpenParenthesisParser.parse(location);
		if (pr1.success() && (pr1.end().codepointAtOffset(0) == ')'))
		{
			MutableLocation n = new MutableLocation(pr1.end());
			n.advance();
			return new IdentifierNode(location, n);
		}
		
		ParseResult pr2 = IdentifierParser.parse(location);
		if (pr2.success())
			return pr2;
		
		ParseResult pr3 = OperatorParser.parse(location);
		if (pr3.success())
			return pr3;
		
		return combineParseErrors(pr2, pr3);
	}
}
