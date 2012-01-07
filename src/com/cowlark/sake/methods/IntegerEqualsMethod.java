package com.cowlark.sake.methods;

import com.cowlark.sake.types.BooleanType;
import com.cowlark.sake.types.IntegerType;

public class IntegerEqualsMethod extends Method
{
	public IntegerEqualsMethod()
    {
		setSignature("integer.==.1", "integer._equals");
		setReturnType(BooleanType.create());
		setArgumentTypes(IntegerType.create());
    }
}
