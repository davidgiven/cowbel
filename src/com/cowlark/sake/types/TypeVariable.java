package com.cowlark.sake.types;

import com.cowlark.sake.ast.nodes.IdentifierNode;
import com.cowlark.sake.ast.nodes.Node;
import com.cowlark.sake.errors.CompilationException;
import com.cowlark.sake.methods.Method;

public class TypeVariable extends Type
{
	public static TypeVariable create()
	{
		return new TypeVariable();
	}
	
	private Type _realType;
	
	private TypeVariable()
    {
    }
	
	@Override
	public boolean isVoidType()
	{
		return getRealType().isVoidType();
	}
	
	@Override
	protected Type getRealType()
	{
		if (_realType == null)
			return this;
		return _realType;
	}
	
	@Override
	public String getCanonicalTypeName()
	{
		if (_realType != null)
			return _realType.getCanonicalTypeName();
	    return "α";
	}

	@Override
	public boolean isConcreteType()
	{
		if (_realType == null)
			return false;
		return _realType.isConcreteType();
	}

	@Override
	public void unifyWithImpl(Node node, Type other) throws CompilationException
	{
		if (_realType != null)
			_realType.unifyWith(node, other);
		else
			_realType = other;
	}
	
	@Override
	public Method lookupMethod(Node node, IdentifierNode id)
	        throws CompilationException
	{
		assert(_realType != null);
		return _realType.lookupMethod(node, id);
	}
}
