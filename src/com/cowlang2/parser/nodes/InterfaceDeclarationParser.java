package com.cowlang2.parser.nodes;

import com.cowlang2.parser.core.Location;
import com.cowlang2.parser.core.ParseResult;
import com.cowlang2.parser.errors.InvalidStatement;

public class InterfaceDeclarationParser extends Parser
{
	@Override
	protected ParseResult parseImpl(Location location)
	{
		return new InvalidStatement(location);
	}
}
