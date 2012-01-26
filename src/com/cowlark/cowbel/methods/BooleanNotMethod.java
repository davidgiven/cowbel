package com.cowlark.cowbel.methods;

import com.cowlark.cowbel.types.BooleanType;

public class BooleanNotMethod extends Method
{
	public BooleanNotMethod()
    {
		setSignature("boolean.!.0", "boolean._not");
		setOutputTypes(BooleanType.create());
		setInputTypes();
    }
}
