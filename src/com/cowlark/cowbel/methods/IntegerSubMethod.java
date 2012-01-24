package com.cowlark.cowbel.methods;

import com.cowlark.cowbel.types.IntegerType;

public class IntegerSubMethod extends Method
{
	public IntegerSubMethod()
    {
		setSignature("integer.-.1", "integer._sub");
		setReturnType(IntegerType.create());
		setArgumentTypes(IntegerType.create());
    }
}
