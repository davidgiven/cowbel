/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING.BSD in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel.methods;

import com.cowlark.cowbel.types.BooleanType;

public class BooleanXorMethod extends PrimitiveMethod
{
	public BooleanXorMethod()
    {
		setSignature("boolean.^.1", "boolean._xor");
		setOutputTypes(BooleanType.create());
		setInputTypes(BooleanType.create());
    }
}
