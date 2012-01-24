package com.cowlark.cowbel.methods;

import com.cowlark.cowbel.types.BooleanType;
import com.cowlark.cowbel.types.IntegerType;

public class IntegerNotEqualsMethod extends Method
{
	public IntegerNotEqualsMethod()
    {
		setSignature("integer.!=.1", "integer._notequals");
		setReturnType(BooleanType.create());
		setArgumentTypes(IntegerType.create());
    }
}
