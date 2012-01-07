package com.cowlark.sake.methods;

import com.cowlark.sake.types.BooleanType;

public class BooleanAndMethod extends Method
{
	public BooleanAndMethod()
    {
		setSignature("boolean.&", "boolean._and");
		setReturnType(BooleanType.create());
		setArgumentTypes(BooleanType.create());
    }
}
