package com.cowlark.cowbel.parser.errors;

import com.cowlark.cowbel.parser.core.FailedParse;
import com.cowlark.cowbel.parser.core.Location;

public class ExpectedAtom extends FailedParse
{
	public ExpectedAtom(Location loc)
    {
	    super(loc);
    }
}
