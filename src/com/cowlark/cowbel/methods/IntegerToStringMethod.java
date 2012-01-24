package com.cowlark.cowbel.methods;

import com.cowlark.cowbel.types.StringType;

public class IntegerToStringMethod extends Method
{
	public IntegerToStringMethod()
    {
		setSignature("integer.toString.0", "integer.toString");
		setReturnType(StringType.create());
		setArgumentTypes();
    }
}
