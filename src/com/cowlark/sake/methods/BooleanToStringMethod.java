package com.cowlark.sake.methods;

import com.cowlark.sake.types.StringType;

public class BooleanToStringMethod extends Method
{
	public BooleanToStringMethod()
    {
		setSignature("boolean.toString.0", "boolean.toString");
		setReturnType(StringType.create());
		setArgumentTypes();
    }
}
