package com.cowlark.sake.types;

import com.cowlark.sake.ast.nodes.IdentifierNode;
import com.cowlark.sake.ast.nodes.MethodCallNode;
import com.cowlark.sake.ast.nodes.Node;
import com.cowlark.sake.errors.CompilationException;
import com.cowlark.sake.errors.NoSuchMethodException;
import com.cowlark.sake.errors.TypesNotCompatibleException;
import com.cowlark.sake.methods.Method;

public abstract class PrimitiveType extends Type
{
	@Override
	protected void unifyWithImpl(Node node, Type other)
	        throws CompilationException
	{
		if (!(getClass().equals(other.getClass())))
			throw new TypesNotCompatibleException(node, this, other);
	}
	
	@Override
	public Method lookupMethod(Node node, IdentifierNode id)
	        throws CompilationException
	{
		MethodCallNode n = (MethodCallNode) node;
		
		String signature = getCanonicalTypeName() + "." + id.getText() +
			"." + n.getMethodArgumentCount();
		
		Method method = Method.lookupPrimitiveMethod(signature);
		if (method == null)
			throw new NoSuchMethodException(node, this, id);
		return method;
	}
}
