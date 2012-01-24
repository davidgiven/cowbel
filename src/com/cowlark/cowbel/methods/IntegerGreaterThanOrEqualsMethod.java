package com.cowlark.cowbel.methods;

import com.cowlark.cowbel.types.BooleanType;
import com.cowlark.cowbel.types.IntegerType;

public class IntegerGreaterThanOrEqualsMethod extends Method
{
	public IntegerGreaterThanOrEqualsMethod()
    {
		setSignature("integer.>=.1", "integer._ge");
		setReturnType(BooleanType.create());
		setArgumentTypes(IntegerType.create());
    }
}
