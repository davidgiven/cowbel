package com.cowlang.sake.parser.errors;

import com.cowlang.sake.parser.core.FailedParse;
import com.cowlang.sake.parser.core.Location;

public class ExpectedSyntacticElement extends FailedParse
{
	private String _element;
	
	public ExpectedSyntacticElement(Location loc, String element)
    {
	    super(loc);
	    _element = element;
    }
}
