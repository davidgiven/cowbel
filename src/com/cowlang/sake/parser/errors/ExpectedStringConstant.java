package com.cowlang.sake.parser.errors;

import com.cowlang.sake.parser.core.FailedParse;
import com.cowlang.sake.parser.core.Location;

public class ExpectedStringConstant extends FailedParse
{
	public ExpectedStringConstant(Location loc)
    {
	    super(loc);
    }
}
