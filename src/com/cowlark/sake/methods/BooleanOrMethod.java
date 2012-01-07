package com.cowlark.sake.methods;

import com.cowlark.sake.types.BooleanType;

public class BooleanOrMethod extends Method
{
	public BooleanOrMethod()
    {
		setSignature("boolean.|.1", "boolean._or");
		setReturnType(BooleanType.create());
		setArgumentTypes(BooleanType.create());
    }
}
