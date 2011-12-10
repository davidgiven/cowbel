package com.cowlark.sake.methods;

import com.cowlark.sake.types.StringType;

public class StringReplaceMethod extends Method
{
	public StringReplaceMethod()
    {
		setSignature("string.replace");
		setReturnType(StringType.create());
		setArgumentTypes(StringType.create(), StringType.create());
    }
}
