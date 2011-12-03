package com.cowlark.sake.types;

public class InferredType extends Type
{
	public InferredType()
    {
    }
	
	@Override
	public String getCanonicalTypeName()
	{
	    return "inferred";
	}
}
