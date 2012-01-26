package com.cowlark.cowbel.parser.parsers;

import com.cowlark.cowbel.parser.core.Location;
import com.cowlark.cowbel.parser.core.ParseResult;
import com.cowlark.cowbel.parser.errors.ExpectedSyntacticElement;

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