package com.cowlark.sake.types;

public class ListType extends Type
{
	public static ListType create(Type child)
	{
		ListType type = new ListType(child);
		return TypeRegistry.canonicalise(type);
	}
	
	private Type _childType;
	
	private ListType(Type child)
    {
		_childType = child;
    }
	
	@Override
	public String getCanonicalTypeName()
	{
	    return "[" + _childType.getCanonicalTypeName() + "]";
	}
}
