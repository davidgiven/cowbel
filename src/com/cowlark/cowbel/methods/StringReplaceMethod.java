package com.cowlark.cowbel.methods;

import com.cowlark.cowbel.types.StringType;

public class StringReplaceMethod extends Method
{
	public StringReplaceMethod()
    {
		setSignature("string.replace.2", "string.replace");
		setOutputTypes(StringType.create());
		setInputTypes(StringType.create(), StringType.create());
    }
}
