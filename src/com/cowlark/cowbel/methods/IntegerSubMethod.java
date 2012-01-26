package com.cowlark.cowbel.methods;

import com.cowlark.cowbel.types.IntegerType;

public class IntegerSubMethod extends Method
{
	public IntegerSubMethod()
    {
		setSignature("integer.-.1", "integer._sub");
		setOutputTypes(IntegerType.create());
		setInputTypes(IntegerType.create());
    }
}
