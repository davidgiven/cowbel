package com.cowlark.sake.types;

public class ListType extends Type
{
	private Type _childType;
	
	public ListType(Type child)
    {
		_childType = child;
    }
	
	@Override
	public String getCanonicalTypeName()
	{
	    return "[" + _childType.getCanonicalTypeName() + "]";
	}
}
