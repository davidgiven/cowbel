/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel.methods;

import com.cowlark.cowbel.types.IntegerType;

public class IntegerNegateMethod extends Method
{
	public IntegerNegateMethod()
    {
		setSignature("integer.-.0", "integer._negate");
		setOutputTypes(IntegerType.create());
		setInputTypes();
    }
}
