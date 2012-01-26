package com.cowlark.cowbel.methods;

import com.cowlark.cowbel.types.IntegerType;
import com.cowlark.cowbel.types.Type;

public class ArraySizeMethod extends TemplatedMethod
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
	        return new ArraySizeMethod(type);
	    }
	}
	
	private static final String SIGNATURE = "array.length.0";
	private static final String IDENTIFIER = "array.length";
	
	public ArraySizeMethod(Type receivertype)
    {
		super(receivertype);
		
		setSignature(SIGNATURE, IDENTIFIER);
		setOutputTypes(IntegerType.create());
		setInputTypes();
    }
}
