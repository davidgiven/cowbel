package com.cowlark.sake.types;

public class StringType extends Type
{
	public StringType()
    {
    }
	
	@Override
	public String getCanonicalTypeName()
	{
	    return "string";
	}
}
