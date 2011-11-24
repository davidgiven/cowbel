package com.cowlark.sake.parser.errors;

import com.cowlark.sake.parser.core.FailedParse;
import com.cowlark.sake.parser.core.Location;

public class InvalidCharacterInStringConstant extends FailedParse
{
	public InvalidCharacterInStringConstant(Location loc)
    {
	    super(loc);
    }
}
