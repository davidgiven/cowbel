package com.cowlark.cowbel.parser.errors;

import com.cowlark.cowbel.parser.core.FailedParse;
import com.cowlark.cowbel.parser.core.Location;

public class ExpectedIdentifier extends FailedParse
{
	public ExpectedIdentifier(Location loc)
    {
	    super(loc);
    }
}
