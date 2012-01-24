package com.cowlark.cowbel.methods;

import com.cowlark.cowbel.types.BooleanType;
import com.cowlark.cowbel.types.IntegerType;

public class IntegerGreaterThanMethod extends Method
{
	public IntegerGreaterThanMethod()
    {
		setSignature("integer.>.1", "integer._gt");
		setReturnType(BooleanType.create());
		setArgumentTypes(IntegerType.create());
    }
}
