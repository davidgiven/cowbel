package com.cowlark.sake.methods;

import com.cowlark.sake.types.BooleanType;
import com.cowlark.sake.types.StringType;

public class StringEqualsMethod extends Method
{
	public StringEqualsMethod()
    {
		setSignature("string.==.1", "string._equals");
		setReturnType(BooleanType.create());
		setArgumentTypes(StringType.create());
    }
}
