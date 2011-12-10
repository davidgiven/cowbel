package com.cowlark.sake.parser.parsers;

import com.cowlark.sake.ast.nodes.ListTypeNode;
import com.cowlark.sake.ast.nodes.StringTypeNode;
import com.cowlark.sake.ast.nodes.TypeNode;
import com.cowlark.sake.parser.core.Location;
import com.cowlark.sake.parser.core.ParseResult;

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
		
		ParseResult childpr = TypeParser.parse(pr2.end());
		if (childpr.failed())
			return combineParseErrors(pr1, childpr);
		
		ParseResult pr4 = CloseSquareParser.parse(childpr.end());
		if (pr4.failed())
			return combineParseErrors(pr1, pr4);
		
		return new ListTypeNode(location, pr4.end(), (TypeNode) childpr);
	}
}
