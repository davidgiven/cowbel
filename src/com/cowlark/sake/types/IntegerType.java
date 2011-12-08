package com.cowlark.sake.types;

public class IntegerType extends Type
{
	private static IntegerType _instance =
		TypeRegistry.canonicalise(new IntegerType());
	
	public static IntegerType create()
	{
		return _instance;
	}
	
	private IntegerType()
    {
    }
	
	@Override
	public String getCanonicalTypeName()
	{
	    return "integer";
	}
}
