package com.cowlark.cowbel.methods;

import com.cowlark.cowbel.types.IntegerType;

public class IntegerNegateMethod extends Method
{
	public IntegerNegateMethod()
    {
		setSignature("integer.-.0", "integer._negate");
		setReturnType(IntegerType.create());
		setArgumentTypes();
    }
}
