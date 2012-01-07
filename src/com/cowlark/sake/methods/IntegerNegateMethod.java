package com.cowlark.sake.methods;

import com.cowlark.sake.types.IntegerType;

public class IntegerNegateMethod extends Method
{
	public IntegerNegateMethod()
    {
		setSignature("integer.-.0", "integer._negate");
		setReturnType(IntegerType.create());
		setArgumentTypes();
    }
}
