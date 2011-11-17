package com.cowlang.sake.parser.nodes;

import com.cowlang.sake.parser.core.Location;
import com.cowlang.sake.parser.core.ParseResult;
import com.cowlang.sake.parser.errors.ExpectedSyntacticElement;

public class TrivialParser extends Parser
{
	private String _template;
	
	public TrivialParser(String c)
    {
		_template = c;
    }
	
	@Override
	protected ParseResult parseImpl(Location location)
	{
		ParseResult pr = AtomParser.parse(location);
		if (pr.success() && pr.getText().equals(_template))
			return pr;
		
		return new ExpectedSyntacticElement(location, _template);
	}
}
