package com.cowlark.cowbel.parser.errors;

import com.cowlark.cowbel.parser.core.FailedParse;
import com.cowlark.cowbel.parser.core.Location;

public class InvalidStatement extends FailedParse
{
	public InvalidStatement(Location loc)
    {
	    super(loc);
    }
}
