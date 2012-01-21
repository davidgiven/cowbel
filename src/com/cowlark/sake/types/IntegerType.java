package com.cowlark.sake.types;

public class IntegerType extends PrimitiveType
{
	private static IntegerType _instance =
		Type.canonicalise(new IntegerType());
	
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
	
	@Override
	public void visit(TypeVisitor visitor)
	{
	    visitor.visit(this);	    
	}
}
