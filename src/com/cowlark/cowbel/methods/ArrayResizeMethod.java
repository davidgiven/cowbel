package com.cowlark.cowbel.methods;

import com.cowlark.cowbel.types.ArrayType;
import com.cowlark.cowbel.types.IntegerType;
import com.cowlark.cowbel.types.Type;
import com.cowlark.cowbel.types.VoidType;

public class ArrayResizeMethod extends TemplatedMethod
{
	public static class Factory extends TemplatedMethod.Factory
	{
		public Factory()
        {
			super(SIGNATURE);
        }
		
		@Override
	    Method create(Type type)
	    {
	        return new ArrayResizeMethod(type);
	    }
	}
	
	private static final String SIGNATURE = "array.resize.2";
	private static final String IDENTIFIER = "array.resize";
	
	public ArrayResizeMethod(Type receivertype)
    {
		super(receivertype);
		
		ArrayType t = (ArrayType) receivertype;
		
		setSignature(SIGNATURE, IDENTIFIER);
		setReturnType(VoidType.create());
		setArgumentTypes(IntegerType.create(), t.getChildType());
    }
}
