package com.cowlang.sake.parser.errors;

import com.cowlang.sake.parser.core.FailedParse;
import com.cowlang.sake.parser.core.Location;

public class InvalidCharacterInStringConstant extends FailedParse
{
	public InvalidCharacterInStringConstant(Location loc)
    {
	    super(loc);
    }
}
