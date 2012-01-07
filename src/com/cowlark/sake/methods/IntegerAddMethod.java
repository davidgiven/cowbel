package com.cowlark.sake.methods;

import com.cowlark.sake.types.IntegerType;

public class IntegerAddMethod extends Method
{
	public IntegerAddMethod()
    {
		setSignature("integer.+.1", "integer._add");
		setReturnType(IntegerType.create());
		setArgumentTypes(IntegerType.create());
    }
}
