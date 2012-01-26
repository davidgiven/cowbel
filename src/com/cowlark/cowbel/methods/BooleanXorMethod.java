package com.cowlark.cowbel.methods;

import com.cowlark.cowbel.types.BooleanType;

public class BooleanXorMethod extends Method
{
	public BooleanXorMethod()
    {
		setSignature("boolean.^.1", "boolean._xor");
		setOutputTypes(BooleanType.create());
		setInputTypes(BooleanType.create());
    }
}
