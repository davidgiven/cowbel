package com.cowlark.sake.parser.errors;

import com.cowlark.sake.parser.core.FailedParse;
import com.cowlark.sake.parser.core.Location;

public class ExpectedStringConstant extends FailedParse
{
	public ExpectedStringConstant(Location loc)
    {
	    super(loc);
    }
}
