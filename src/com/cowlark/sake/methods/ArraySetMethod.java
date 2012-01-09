package com.cowlark.sake.methods;

import com.cowlark.sake.types.ArrayType;
import com.cowlark.sake.types.IntegerType;
import com.cowlark.sake.types.Type;
import com.cowlark.sake.types.VoidType;

public class ArraySetMethod extends TemplatedMethod
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
	        return new ArraySetMethod(type);
	    }
	}
	
	private static final String SIGNATURE = "array.set.2";
	private static final String IDENTIFIER = "array.set";
	
	public ArraySetMethod(Type receivertype)
    {
		super(receivertype);
		
		ArrayType t = (ArrayType) receivertype;
		
		setSignature(SIGNATURE, IDENTIFIER);
		setReturnType(VoidType.create());
		setArgumentTypes(IntegerType.create(), t.getChildType());
    }
}
