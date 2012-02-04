/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING.BSD in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel.methods;

import com.cowlark.cowbel.types.StringType;

public class BooleanToStringMethod extends Method
{
	public BooleanToStringMethod()
    {
		setSignature("boolean.toString.0", "boolean.toString");
		setOutputTypes(StringType.create());
		setInputTypes();
    }
}
