package com.cowlark.sake.types;

public class VoidType extends PrimitiveType
{
	private static VoidType _instance =
		Type.canonicalise(new VoidType());
	
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
