package com.cowlark.sake.parser.errors;

import com.cowlark.sake.parser.core.FailedParse;
import com.cowlark.sake.parser.core.Location;

public class ExpectedSyntacticElement extends FailedParse
{
	private String _element;
	
	public ExpectedSyntacticElement(Location loc, String element)
    {
	    super(loc);
	    _element = element;
    }
}
