package com.cowlark.cowbel.methods;

import com.cowlark.cowbel.types.IntegerType;

public class IntegerAddMethod extends Method
{
	public IntegerAddMethod()
    {
		setSignature("integer.+.1", "integer._add");
		setReturnType(IntegerType.create());
		setArgumentTypes(IntegerType.create());
    }
}
