package com.cowlark.sake.methods;

import com.cowlark.sake.types.BooleanType;
import com.cowlark.sake.types.IntegerType;

public class IntegerGreaterThanMethod extends Method
{
	public IntegerGreaterThanMethod()
    {
		setSignature("integer.>.1", "integer._gt");
		setReturnType(BooleanType.create());
		setArgumentTypes(IntegerType.create());
    }
}
