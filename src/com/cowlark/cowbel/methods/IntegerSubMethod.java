/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING.BSD in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel.methods;

import com.cowlark.cowbel.types.IntegerType;

public class IntegerSubMethod extends Method
{
	public IntegerSubMethod()
    {
		setSignature("integer.-.1", "integer._sub");
		setOutputTypes(IntegerType.create());
		setInputTypes(IntegerType.create());
    }
}
