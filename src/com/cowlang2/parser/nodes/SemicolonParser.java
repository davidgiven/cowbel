package com.cowlang2.parser.nodes;

import com.cowlang2.parser.core.Location;
import com.cowlang2.parser.core.ParseResult;
import com.cowlang2.parser.errors.ExpectedSyntacticElement;

public class SemicolonParser extends Parser
{
	public static SemicolonParser Instance = new SemicolonParser();
	
	@Override
	protected ParseResult parseImpl(Location location)
	{
		ParseResult pr = AtomParser.Instance.parse(location);
		if (pr.success() && pr.getText().equals(";"))
			return pr;
		
		return new ExpectedSyntacticElement(location, ";");
	}
}
