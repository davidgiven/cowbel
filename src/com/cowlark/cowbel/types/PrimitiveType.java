package com.cowlark.cowbel.types;

import com.cowlark.cowbel.ast.nodes.IdentifierNode;
import com.cowlark.cowbel.ast.nodes.MethodCallNode;
import com.cowlark.cowbel.ast.nodes.Node;
import com.cowlark.cowbel.errors.CompilationException;
import com.cowlark.cowbel.errors.NoSuchMethodException;
import com.cowlark.cowbel.errors.TypesNotCompatibleException;
import com.cowlark.cowbel.methods.Method;

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
