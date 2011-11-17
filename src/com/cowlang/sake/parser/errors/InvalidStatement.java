package com.cowlang.sake.parser.errors;

import com.cowlang.sake.parser.core.FailedParse;
import com.cowlang.sake.parser.core.Location;

public class InvalidStatement extends FailedParse
{
	public InvalidStatement(Location loc)
    {
	    super(loc);
    }
}
