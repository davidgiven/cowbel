package com.cowlark.sake.errors;

import com.cowlark.sake.parser.core.FailedParse;

public class FailedParseException extends CompilationException
{
    private static final long serialVersionUID = -2127671112554932498L;

    private FailedParse _fp;
    
	public FailedParseException(FailedParse fp)
    {
		_fp = fp;
    }
	
	public FailedParse getFailedParse()
	{
		return _fp;
	}
}
