/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING.BSD in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel.types;

import java.util.HashMap;
import java.util.Map;
import com.cowlark.cowbel.ast.HasInputs;
import com.cowlark.cowbel.ast.HasTypeArguments;
import com.cowlark.cowbel.ast.IsMethod;
import com.cowlark.cowbel.ast.nodes.IdentifierNode;
import com.cowlark.cowbel.ast.nodes.Node;
import com.cowlark.cowbel.errors.CompilationException;
import com.cowlark.cowbel.errors.FailedToInferTypeException;
import com.cowlark.cowbel.methods.Method;

public abstract class Type
{
	private static int _globalid = 0;
	private static Map<String, Type> _typeMap =
		new HashMap<String, Type>();

	@SuppressWarnings("unchecked")
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
	
	private int _id = _globalid++;
	
	public abstract String getCanonicalTypeName();
	
	public int getId()
    {
	    return _id;
    }
	
	@Override
	public String toString()
	{
	    return getCanonicalTypeName(); 
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

		/* Ensure that if one of the types is an InferredType, it is always
		 * the receiver. */
		
		if (t2 instanceof InferredType)
			t2.unifyWithImpl(node, t1);
		else
			t1.unifyWithImpl(node, t2);
	}

	public void ensureConcrete(Node node) throws CompilationException
	{
		if (!isConcreteType())
			throw new FailedToInferTypeException(node, this);
	}
	
	public abstract <T extends Node & IsMethod & HasInputs & HasTypeArguments>
		Method lookupMethod(T node, IdentifierNode id)
			throws CompilationException;
	
	public abstract void visit(TypeVisitor visitor);
}
