package com.cowlang2.parser.errors;

import com.cowlang2.parser.core.FailedParse;
import com.cowlang2.parser.core.Location;

public class ExpectedSyntacticElement extends FailedParse
{
	private String _element;
	
	public ExpectedSyntacticElement(Location loc, String element)
    {
	    super(loc);
	    _element = element;
    }
}
