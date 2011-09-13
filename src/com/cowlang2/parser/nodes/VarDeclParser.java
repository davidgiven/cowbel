package com.cowlang2.parser.nodes;

import com.cowlang2.parser.core.Location;
import com.cowlang2.parser.core.ParseResult;
import com.cowlang2.parser.errors.UnimplementedParse;

public class VarDeclParser extends Parser
{
	public static VarDeclParser Instance = new VarDeclParser();
	
	@Override
	protected ParseResult parseImpl(Location location)
	{
		return new UnimplementedParse(location); 
	}
}
