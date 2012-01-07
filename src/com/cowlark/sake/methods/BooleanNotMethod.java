package com.cowlark.sake.methods;

import com.cowlark.sake.types.BooleanType;

public class BooleanNotMethod extends Method
{
	public BooleanNotMethod()
    {
		setSignature("boolean.!", "boolean._not");
		setReturnType(BooleanType.create());
		setArgumentTypes();
    }
}
