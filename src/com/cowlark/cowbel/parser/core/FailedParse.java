/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel.parser.core;

public class FailedParse extends ParseResult
{
	public FailedParse(Location loc)
    {
		super(loc, loc);
    }
	
	@Override
	public boolean failed()
	{
	    return true;
	}
	
	public String message()
	{
		return getClass().toString();
	}
}
