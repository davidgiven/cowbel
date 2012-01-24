package com.cowlark.cowbel.parser.errors;

import com.cowlark.cowbel.parser.core.FailedParse;
import com.cowlark.cowbel.parser.core.Location;

public class UnimplementedParse extends FailedParse
{
	public UnimplementedParse(Location loc)
    {
	    super(loc);
    }
}
