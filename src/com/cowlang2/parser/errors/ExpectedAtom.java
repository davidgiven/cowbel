package com.cowlang2.parser.errors;

import com.cowlang2.parser.core.FailedParse;
import com.cowlang2.parser.core.Location;

public class ExpectedAtom extends FailedParse
{
	public ExpectedAtom(Location loc)
    {
	    super(loc);
    }
}
