package com.cowlark.sake.types;

public class VoidType extends Type
{
	public VoidType()
    {
    }
	
	@Override
	public String getCanonicalTypeName()
	{
	    return "void";
	}
}
