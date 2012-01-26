/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel.parser.core;

public class Token extends ParseResult
{
	public Token(Location start, Location end)
    {
		super(start, end);
    }
	
	@Override
	public boolean failed()
	{
	    return false;
	}
}
