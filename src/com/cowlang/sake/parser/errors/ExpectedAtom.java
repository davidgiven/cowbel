package com.cowlang.sake.parser.errors;

import com.cowlang.sake.parser.core.FailedParse;
import com.cowlang.sake.parser.core.Location;

public class ExpectedAtom extends FailedParse
{
	public ExpectedAtom(Location loc)
    {
	    super(loc);
    }
}
