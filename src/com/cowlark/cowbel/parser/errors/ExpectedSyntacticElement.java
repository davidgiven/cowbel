/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING.BSD in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel.parser.errors;

import com.cowlark.cowbel.parser.core.FailedParse;
import com.cowlark.cowbel.parser.core.Location;

public class ExpectedSyntacticElement extends FailedParse
{
	private String _element;
	
	public ExpectedSyntacticElement(Location loc, String element)
    {
	    super(loc);
	    _element = element;
    }
}
