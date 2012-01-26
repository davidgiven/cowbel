package com.cowlark.cowbel.methods;

import com.cowlark.cowbel.types.ArrayType;
import com.cowlark.cowbel.types.IntegerType;
import com.cowlark.cowbel.types.Type;

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
		setOutputTypes();
		setInputTypes(IntegerType.create(), t.getChildType());
    }
}
