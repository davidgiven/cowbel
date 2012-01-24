package com.cowlark.cowbel.methods;

import com.cowlark.cowbel.types.StringType;

public class BooleanToStringMethod extends Method
{
	public BooleanToStringMethod()
    {
		setSignature("boolean.toString.0", "boolean.toString");
		setReturnType(StringType.create());
		setArgumentTypes();
    }
}
