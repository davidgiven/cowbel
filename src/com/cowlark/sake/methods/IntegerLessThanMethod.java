package com.cowlark.sake.methods;

import com.cowlark.sake.types.BooleanType;
import com.cowlark.sake.types.IntegerType;

public class IntegerLessThanMethod extends Method
{
	public IntegerLessThanMethod()
    {
		setSignature("integer.<.1", "integer._lt");
		setReturnType(BooleanType.create());
		setArgumentTypes(IntegerType.create());
    }
}
