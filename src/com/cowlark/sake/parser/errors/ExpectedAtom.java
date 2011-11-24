package com.cowlark.sake.parser.errors;

import com.cowlark.sake.parser.core.FailedParse;
import com.cowlark.sake.parser.core.Location;

public class ExpectedAtom extends FailedParse
{
	public ExpectedAtom(Location loc)
    {
	    super(loc);
    }
}
