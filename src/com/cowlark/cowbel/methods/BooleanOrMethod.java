package com.cowlark.cowbel.methods;

import com.cowlark.cowbel.types.BooleanType;

public class BooleanOrMethod extends Method
{
	public BooleanOrMethod()
    {
		setSignature("boolean.|.1", "boolean._or");
		setOutputTypes(BooleanType.create());
		setInputTypes(BooleanType.create());
    }
}
