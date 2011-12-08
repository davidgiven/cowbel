package com.cowlark.sake.types;

public class VoidType extends Type
{
	private static VoidType _instance =
		TypeRegistry.canonicalise(new VoidType());
	
	public static VoidType create()
	{
		return _instance;
	}
	
	private VoidType()
    {
    }
	
	@Override
	public String getCanonicalTypeName()
	{
	    return "void";
	}
}
