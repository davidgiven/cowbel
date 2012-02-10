/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING.BSD in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel;

import com.cowlark.cowbel.types.BooleanType;
import com.cowlark.cowbel.types.ExternType;
import com.cowlark.cowbel.types.IntegerType;
import com.cowlark.cowbel.types.RealType;
import com.cowlark.cowbel.types.StringType;
import com.cowlark.cowbel.types.Type;

public class S
{
	public static final Type[] ROOT_TYPES = {
		ExternType.create(),
		IntegerType.create(),
		BooleanType.create(),
		RealType.create(),
		StringType.create()
	};
}
