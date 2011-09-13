package com.cowlang2.parser.errors;

import com.cowlang2.parser.core.FailedParse;
import com.cowlang2.parser.core.Location;

public class InvalidStatement extends FailedParse
{
	public InvalidStatement(Location loc)
    {
	    super(loc);
    }
}
