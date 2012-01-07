package com.cowlark.sake.methods;

import com.cowlark.sake.types.IntegerType;

public class IntegerSubMethod extends Method
{
	public IntegerSubMethod()
    {
		setSignature("integer.-.1", "integer._sub");
		setReturnType(IntegerType.create());
		setArgumentTypes(IntegerType.create());
    }
}
