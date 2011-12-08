package com.cowlark.sake.types;

public class StringType extends Type
{
	private static StringType _instance =
		TypeRegistry.canonicalise(new StringType());
	
	public static StringType create()
	{
		return _instance;
	}
	
	private StringType()
    {
    }
	
	@Override
	public String getCanonicalTypeName()
	{
	    return "string";
	}
}
