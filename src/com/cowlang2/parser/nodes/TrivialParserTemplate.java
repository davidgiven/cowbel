package com.cowlang2.parser.nodes;

import com.cowlang2.parser.core.Location;
import com.cowlang2.parser.core.ParseResult;
import com.cowlang2.parser.errors.ExpectedSyntacticElement;

public class TrivialParserTemplate extends Parser
{
	private String _template;
	
	public TrivialParserTemplate(String c)
    {
		_template = c;
    }
	
	@Override
	protected ParseResult parseImpl(Location location)
	{
		ParseResult pr = AtomParser.Instance.parse(location);
		if (pr.success() && pr.getText().equals(_template))
			return pr;
		
		return new ExpectedSyntacticElement(location, _template);
	}
}
