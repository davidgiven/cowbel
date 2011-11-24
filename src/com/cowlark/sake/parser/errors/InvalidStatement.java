package com.cowlark.sake.parser.errors;

import com.cowlark.sake.parser.core.FailedParse;
import com.cowlark.sake.parser.core.Location;

public class InvalidStatement extends FailedParse
{
	public InvalidStatement(Location loc)
    {
	    super(loc);
    }
}
