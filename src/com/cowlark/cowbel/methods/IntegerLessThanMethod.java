package com.cowlark.cowbel.methods;

import com.cowlark.cowbel.types.BooleanType;
import com.cowlark.cowbel.types.IntegerType;

public class IntegerLessThanMethod extends Method
{
	public IntegerLessThanMethod()
    {
		setSignature("integer.<.1", "integer._lt");
		setReturnType(BooleanType.create());
		setArgumentTypes(IntegerType.create());
    }
}
