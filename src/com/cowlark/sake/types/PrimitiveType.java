package com.cowlark.sake.types;

import com.cowlark.sake.ast.nodes.Node;
import com.cowlark.sake.errors.CompilationException;
import com.cowlark.sake.errors.TypesNotCompatibleException;

public abstract class PrimitiveType extends Type
{
	@Override
	protected void unifyWithImpl(Node node, Type other)
	        throws CompilationException
	{
		if (!(getClass().equals(other.getClass())))
			throw new TypesNotCompatibleException(node, this, other);
	}
}
