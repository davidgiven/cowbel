package com.cowlark.sake.types;

import com.cowlark.sake.ast.nodes.IdentifierNode;
import com.cowlark.sake.ast.nodes.Node;
import com.cowlark.sake.errors.CompilationException;
import com.cowlark.sake.errors.NoSuchMethodException;
import com.cowlark.sake.errors.TypesNotCompatibleException;
import com.cowlark.sake.methods.Method;

public class ListType extends Type
{
	public static ListType create(Type child)
	{
		ListType type = new ListType(child.getRealType());
		return Type.canonicalise(type);
	}
	
	public static ListType create()
	{
		return create(TypeVariable.create());
	}
	
	private Type _childType;
	
	private ListType(Type child)
    {
		_childType = child;
    }
	
	public Type getChildType()
    {
	    return _childType;
    }
	
	@Override
	public String getCanonicalTypeName()
	{
	    return "[" + _childType.getCanonicalTypeName() + "]";
	}
	
	@Override
	public boolean isConcreteType()
	{
		return _childType.isConcreteType();
	}
	
	@Override
	protected void unifyWithImpl(Node node, Type other)
	        throws CompilationException
	{
		if (!(other instanceof ListType))
			throw new TypesNotCompatibleException(node, this, other);
		
		ListType t = (ListType) other;
		_childType.unifyWith(node, t.getChildType());
	}
	
	@Override
	public Method lookupMethod(Node node, IdentifierNode id)
	        throws CompilationException
	{
		throw new NoSuchMethodException(node, this, id);
	}
}
