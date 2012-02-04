/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING.BSD in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel.parser.core;


public class EOFToken extends TextToken
{
	public EOFToken(Location location)
    {
	    super(location, location);
    }
}
