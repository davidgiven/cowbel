package com.cowlark.sake.types;

public class BooleanType extends Type
{
	private static BooleanType _instance =
		TypeRegistry.canonicalise(new BooleanType());
	
	public static BooleanType create()
	{
		return _instance;
	}
	
	private BooleanType()
    {
    }
	
	@Override
	public String getCanonicalTypeName()
	{
	    return "boolean";
	}
}
