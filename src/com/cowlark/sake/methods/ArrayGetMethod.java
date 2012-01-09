package com.cowlark.sake.methods;

import com.cowlark.sake.types.ArrayType;
import com.cowlark.sake.types.IntegerType;
import com.cowlark.sake.types.Type;

public class ArrayGetMethod extends TemplatedMethod
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
	        return new ArrayGetMethod(type);
	    }
	}
	
	private static final String SIGNATURE = "array.get.1";
	private static final String IDENTIFIER = "array.get";
	
	public ArrayGetMethod(Type receivertype)
    {
		super(receivertype);
		
		ArrayType t = (ArrayType) receivertype;
		
		setSignature(SIGNATURE, IDENTIFIER);
		setReturnType(t.getChildType());
		setArgumentTypes(IntegerType.create());
    }
}
