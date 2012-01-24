package com.cowlark.cowbel.types;

public class StringType extends PrimitiveType
{
	private static StringType _instance =
		Type.canonicalise(new StringType());
	
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
	
	@Override
	public void visit(TypeVisitor visitor)
	{
	    visitor.visit(this);	    
	}
}
