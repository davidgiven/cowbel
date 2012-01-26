/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel.parser.errors;

import com.cowlark.cowbel.parser.core.FailedParse;
import com.cowlark.cowbel.parser.core.Location;

public class ExpectedStringConstant extends FailedParse
{
	public ExpectedStringConstant(Location loc)
    {
	    super(loc);
    }
}
