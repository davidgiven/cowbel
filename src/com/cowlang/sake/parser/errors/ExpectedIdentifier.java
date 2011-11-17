package com.cowlang.sake.parser.errors;

import com.cowlang.sake.parser.core.FailedParse;
import com.cowlang.sake.parser.core.Location;

public class ExpectedIdentifier extends FailedParse
{
	public ExpectedIdentifier(Location loc)
    {
	    super(loc);
    }
}
