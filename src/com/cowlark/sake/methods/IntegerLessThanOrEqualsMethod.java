package com.cowlark.sake.methods;

import com.cowlark.sake.types.BooleanType;
import com.cowlark.sake.types.IntegerType;

public class IntegerLessThanOrEqualsMethod extends Method
{
	public IntegerLessThanOrEqualsMethod()
    {
		setSignature("integer.<=.1", "integer._le");
		setReturnType(BooleanType.create());
		setArgumentTypes(IntegerType.create());
    }
}
