package com.cowlark.sake.parser.errors;

import com.cowlark.sake.parser.core.FailedParse;
import com.cowlark.sake.parser.core.Location;

public class ExpectedIdentifier extends FailedParse
{
	public ExpectedIdentifier(Location loc)
    {
	    super(loc);
    }
}
