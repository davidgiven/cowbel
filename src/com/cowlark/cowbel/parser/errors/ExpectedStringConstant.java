package com.cowlark.cowbel.parser.errors;

import com.cowlark.cowbel.parser.core.FailedParse;
import com.cowlark.cowbel.parser.core.Location;

public class ExpectedStringConstant extends FailedParse
{
	public ExpectedStringConstant(Location loc)
    {
	    super(loc);
    }
}
