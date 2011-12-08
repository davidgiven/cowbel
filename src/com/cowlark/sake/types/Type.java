package com.cowlark.sake.types;

import com.cowlark.sake.ast.nodes.Node;
import com.cowlark.sake.errors.CompilationException;
import com.cowlark.sake.errors.TypesNotCompatibleException;

public abstract class Type
{
	public abstract String getCanonicalTypeName();
	
	@Override
	public String toString()
	{
	    return super.toString() + "=" + getCanonicalTypeName(); 
	}
	
	public void checkCompatibilityWith(Node node, Type other) throws CompilationException
	{
		this.checkType(node);
		other.checkType(node);

		if (this != other)
			throw new TypesNotCompatibleException(node, this, other);
	}
	
	public void checkType(Node node) throws CompilationException
	{
	}
}
