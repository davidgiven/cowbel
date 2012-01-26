/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel.methods;

import com.cowlark.cowbel.types.BooleanType;

public class BooleanNotMethod extends Method
{
	public BooleanNotMethod()
    {
		setSignature("boolean.!.0", "boolean._not");
		setOutputTypes(BooleanType.create());
		setInputTypes();
    }
}
