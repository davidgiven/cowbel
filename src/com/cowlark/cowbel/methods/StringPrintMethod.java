package com.cowlark.cowbel.methods;

import com.cowlark.cowbel.types.VoidType;

public class StringPrintMethod extends Method
{
	public StringPrintMethod()
    {
		setSignature("string.print.0", "string.print");
		setReturnType(VoidType.create());
		setArgumentTypes();
    }
}
