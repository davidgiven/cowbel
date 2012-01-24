package com.cowlark.cowbel.methods;

import com.cowlark.cowbel.types.BooleanType;

public class BooleanOrMethod extends Method
{
	public BooleanOrMethod()
    {
		setSignature("boolean.|.1", "boolean._or");
		setReturnType(BooleanType.create());
		setArgumentTypes(BooleanType.create());
    }
}
