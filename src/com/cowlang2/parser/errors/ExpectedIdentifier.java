package com.cowlang2.parser.errors;

import com.cowlang2.parser.core.FailedParse;
import com.cowlang2.parser.core.Location;

public class ExpectedIdentifier extends FailedParse
{
	public ExpectedIdentifier(Location loc)
    {
	    super(loc);
    }
}
