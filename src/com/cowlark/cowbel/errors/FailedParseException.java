/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING.BSD in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel.errors;

import com.cowlark.cowbel.parser.core.FailedParse;

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
