package com.cowlark.cowbel.parser.errors;

import com.cowlark.cowbel.parser.core.FailedParse;
import com.cowlark.cowbel.parser.core.Location;

public class InvalidCharacterInStringConstant extends FailedParse
{
	public InvalidCharacterInStringConstant(Location loc)
    {
	    super(loc);
    }
}
