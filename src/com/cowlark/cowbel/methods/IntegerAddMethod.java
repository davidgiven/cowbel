/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING.BSD in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel.methods;

import com.cowlark.cowbel.types.IntegerType;

public class IntegerAddMethod extends PrimitiveMethod
{
	public IntegerAddMethod()
    {
		setSignature("integer.+.1", "integer._add");
		setOutputTypes(IntegerType.create());
		setInputTypes(IntegerType.create());
    }
}
