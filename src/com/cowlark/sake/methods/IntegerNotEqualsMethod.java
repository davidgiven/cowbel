package com.cowlark.sake.methods;

import com.cowlark.sake.types.BooleanType;
import com.cowlark.sake.types.IntegerType;

public class IntegerNotEqualsMethod extends Method
{
	public IntegerNotEqualsMethod()
    {
		setSignature("integer.!=.1", "integer._notequals");
		setReturnType(BooleanType.create());
		setArgumentTypes(IntegerType.create());
    }
}
