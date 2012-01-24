package com.cowlark.cowbel.types;

public class BooleanType extends PrimitiveType
{
	private static BooleanType _instance =
		Type.canonicalise(new BooleanType());
	
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
	
	@Override
	public void visit(TypeVisitor visitor)
	{
	    visitor.visit(this);	    
	}
}
