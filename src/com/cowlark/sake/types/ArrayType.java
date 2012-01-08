package com.cowlark.sake.types;

import com.cowlark.sake.ast.nodes.IdentifierNode;
import com.cowlark.sake.ast.nodes.MethodCallNode;
import com.cowlark.sake.ast.nodes.Node;
import com.cowlark.sake.errors.CompilationException;
import com.cowlark.sake.errors.NoSuchMethodException;
import com.cowlark.sake.errors.TypesNotCompatibleException;
import com.cowlark.sake.methods.Method;

public class ArrayType extends Type
{
	public static ArrayType create(Type child)
	{
		ArrayType type = new ArrayType(child.getRealType());
		return Type.canonicalise(type);
	}
	
	public static ArrayType create()
	{
		return create(TypeVariable.create());
	}
	
	private Type _childType;
	
	private ArrayType(Type child)
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
		if (!(other instanceof ArrayType))
			throw new TypesNotCompatibleException(node, this, other);
		
		ArrayType t = (ArrayType) other;
		_childType.unifyWith(node, t.getChildType());
	}
	
	@Override
	public Method lookupMethod(Node node, IdentifierNode id)
	        throws CompilationException
	{
		MethodCallNode n = (MethodCallNode) node;
		
		String signature = "array." + id.getText() +
			"." + n.getMethodArgumentCount();
		
		Method method = Method.lookupTypeFamilyMethod(
				n.getMethodReceiver().getType().getRealType(), signature);
		if (method == null)
			throw new NoSuchMethodException(node, this, id);
		return method;
	}
}
