package com.cowlark.sake.parser.errors;

import com.cowlark.sake.parser.core.FailedParse;
import com.cowlark.sake.parser.core.Location;

public class UnimplementedParse extends FailedParse
{
	public UnimplementedParse(Location loc)
    {
	    super(loc);
    }
}
