package com.cowlang2.parser.nodes;

import com.cowlang2.parser.core.Location;
import com.cowlang2.parser.core.ParseResult;
import com.cowlang2.parser.errors.ExpectedSyntacticElement;

public class CloseParenthesisParser extends Parser
{
	public static CloseParenthesisParser Instance = new CloseParenthesisParser();
	
	@Override
	protected ParseResult parseImpl(Location location)
	{
		ParseResult pr = AtomParser.Instance.parse(location);
		if (pr.success() && pr.getText().equals(")"))
			return pr;
		
		return new ExpectedSyntacticElement(location, ")");
	}
}
