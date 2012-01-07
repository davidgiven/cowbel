package com.cowlark.sake.methods;

import com.cowlark.sake.types.BooleanType;

public class BooleanNotMethod extends Method
{
	public BooleanNotMethod()
    {
		setSignature("boolean.!.0", "boolean._not");
		setReturnType(BooleanType.create());
		setArgumentTypes();
    }
}
