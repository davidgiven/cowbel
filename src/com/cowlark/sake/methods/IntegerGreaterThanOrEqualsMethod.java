package com.cowlark.sake.methods;

import com.cowlark.sake.types.BooleanType;
import com.cowlark.sake.types.IntegerType;

public class IntegerGreaterThanOrEqualsMethod extends Method
{
	public IntegerGreaterThanOrEqualsMethod()
    {
		setSignature("integer.>=.1", "integer._ge");
		setReturnType(BooleanType.create());
		setArgumentTypes(IntegerType.create());
    }
}
