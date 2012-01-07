package com.cowlark.sake.methods;

import com.cowlark.sake.types.BooleanType;

public class BooleanXorMethod extends Method
{
	public BooleanXorMethod()
    {
		setSignature("boolean.^", "boolean._xor");
		setReturnType(BooleanType.create());
		setArgumentTypes(BooleanType.create());
    }
}
