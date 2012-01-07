package com.cowlark.sake.methods;

import com.cowlark.sake.types.StringType;

public class StringAddMethod extends Method
{
	public StringAddMethod()
    {
		setSignature("string.+", "string._add");
		setReturnType(StringType.create());
		setArgumentTypes(StringType.create());
    }
}
