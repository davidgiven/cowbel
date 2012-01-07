package com.cowlark.sake.parser.errors;

import com.cowlark.sake.parser.core.FailedParse;
import com.cowlark.sake.parser.core.Location;

public class MalformedIntegerConstant extends FailedParse
{
	public MalformedIntegerConstant(Location loc)
    {
	    super(loc);
    }
}
