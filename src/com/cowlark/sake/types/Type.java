package com.cowlark.sake.types;

import java.util.HashMap;
import java.util.Map;
import com.cowlark.sake.ast.nodes.IdentifierNode;
import com.cowlark.sake.ast.nodes.Node;
import com.cowlark.sake.errors.CompilationException;
import com.cowlark.sake.errors.FailedToInferTypeException;
import com.cowlark.sake.methods.Method;

public abstract class Type
{
	private static Map<String, Type> _typeMap =
		new HashMap<String, Type>();

	protected static <T extends Type> T canonicalise(T candidate)
    {
		if (candidate.isConcreteType())
		{
	    	String canonicalName = candidate.getCanonicalTypeName();
	    	Type type = _typeMap.get(canonicalName);
	    	if (type != null)
	    		return (T) type;
	    	
	    	_typeMap.put(canonicalName, candidate);
		}
		
    	return candidate;
    }
	
	public abstract String getCanonicalTypeName();
	
	@Override
	public String toString()
	{
	    return super.toString() + "=" + getCanonicalTypeName(); 
	}
	
	public boolean isConcreteType()
	{
		return true;
	}
	
	public boolean isVoidType()
	{
		return false;
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		
		if (!(obj instanceof Type))
			return false;
		
		return equals((Type) obj);
	}
	
	public boolean equals(Type other)
	{
		if (!(getClass().equals(other.getClass())))
			return false;
		
		return getCanonicalTypeName().equals(other.getCanonicalTypeName());
	}
	
	protected Type getRealType()
	{
		return this;
	}
	
	protected abstract void unifyWithImpl(Node node, Type other) throws CompilationException;
	
	public void unifyWith(Node node, Type other) throws CompilationException
	{
		Type t1 = getRealType();
		Type t2 = other.getRealType();

		/* Ensure that if one of the types is an TypeVariable, it is always
		 * the receiver. */
		
		if (t2 instanceof TypeVariable)
			t2.unifyWithImpl(node, t1);
		else
			t1.unifyWithImpl(node, t2);
	}

	public void ensureConcrete(Node node) throws CompilationException
	{
		if (!isConcreteType())
			throw new FailedToInferTypeException(node, this);
	}
	
	public abstract Method lookupMethod(Node node, IdentifierNode id)
		throws CompilationException;
	
	public abstract void visit(TypeVisitor visitor);
}
