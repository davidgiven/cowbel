package com.cowlang.sake.parser.errors;

import com.cowlang.sake.parser.core.FailedParse;
import com.cowlang.sake.parser.core.Location;

public class UnimplementedParse extends FailedParse
{
	public UnimplementedParse(Location loc)
    {
	    super(loc);
    }
}
