package com.cowlark.sake.methods;

import com.cowlark.sake.types.VoidType;

public class StringPrintMethod extends Method
{
	public StringPrintMethod()
    {
		setSignature("string.print.0", "string.print");
		setReturnType(VoidType.create());
		setArgumentTypes();
    }
}
