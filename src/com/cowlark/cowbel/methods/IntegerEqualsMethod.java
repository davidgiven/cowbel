package com.cowlark.cowbel.methods;

import com.cowlark.cowbel.types.BooleanType;
import com.cowlark.cowbel.types.IntegerType;

public class IntegerEqualsMethod extends Method
{
	public IntegerEqualsMethod()
    {
		setSignature("integer.==.1", "integer._equals");
		setReturnType(BooleanType.create());
		setArgumentTypes(IntegerType.create());
    }
}
