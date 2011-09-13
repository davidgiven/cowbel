package com.cowlang2.parser.errors;

import com.cowlang2.parser.core.FailedParse;
import com.cowlang2.parser.core.Location;

public class UnimplementedParse extends FailedParse
{
	public UnimplementedParse(Location loc)
    {
	    super(loc);
    }
}
