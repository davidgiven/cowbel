package com.cowlark.sake.methods;

import com.cowlark.sake.types.StringType;

public class IntegerToStringMethod extends Method
{
	public IntegerToStringMethod()
    {
		setSignature("integer.toString.0", "integer.toString");
		setReturnType(StringType.create());
		setArgumentTypes();
    }
}
