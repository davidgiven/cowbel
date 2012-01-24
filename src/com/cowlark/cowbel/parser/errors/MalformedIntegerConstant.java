package com.cowlark.cowbel.parser.errors;

import com.cowlark.cowbel.parser.core.FailedParse;
import com.cowlark.cowbel.parser.core.Location;

public class MalformedIntegerConstant extends FailedParse
{
	public MalformedIntegerConstant(Location loc)
    {
	    super(loc);
    }
}
