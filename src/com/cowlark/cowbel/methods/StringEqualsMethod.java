package com.cowlark.cowbel.methods;

import com.cowlark.cowbel.types.BooleanType;
import com.cowlark.cowbel.types.StringType;

public class StringEqualsMethod extends Method
{
	public StringEqualsMethod()
    {
		setSignature("string.==.1", "string._equals");
		setOutputTypes(BooleanType.create());
		setInputTypes(StringType.create());
    }
}
