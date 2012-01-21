package com.cowlark.sake.types;

import java.util.LinkedList;
import java.util.List;
import com.cowlark.sake.ast.nodes.IdentifierNode;
import com.cowlark.sake.ast.nodes.Node;
import com.cowlark.sake.errors.CompilationException;
import com.cowlark.sake.errors.NoSuchMethodException;
import com.cowlark.sake.errors.TypesNotCompatibleException;
import com.cowlark.sake.methods.Method;

public class FunctionType extends PrimitiveType
{
	public static FunctionType create(List<Type> arguments, Type returntype)
	{
		FunctionType type = new FunctionType(arguments, returntype);
		return Type.canonicalise(type);
	}
	
	public static FunctionType createVoidVoid()
	{
		List<Type> emptylist = new LinkedList<Type>();
		return create(emptylist, VoidType.create());
	}
	
	private List<Type> _arguments;
	private Type _returntype;
	
	private FunctionType(List<Type> arguments, Type returntype)
    {
		_arguments = arguments;
		_returntype = returntype;
    }
	
	@Override
	public String getCanonicalTypeName()
	{
		StringBuilder sb = new StringBuilder();
		sb.append('(');
		
		if (_arguments.isEmpty())
		{
			sb.append("void");
		}
		else
		{
			boolean first = true;
			for (Type t : _arguments)
			{
				if (!first)
					sb.append(", ");
				sb.append(t.getCanonicalTypeName());
				first = false;
			}
		}
		
		sb.append(" -> ");
		sb.append(_returntype.getCanonicalTypeName());
		sb.append(")");
		return sb.toString();
	}
	
	public List<Type> getArgumentTypes()
	{
		return _arguments;
	}
	
	public Type getReturnType()
	{
		return _returntype;
	}
	
	@Override
	protected void unifyWithImpl(Node node, Type other)
	        throws CompilationException
	{
	    super.unifyWithImpl(node, other);
	    
	    if (!(getCanonicalTypeName().equals(other.getCanonicalTypeName())))
	    	throw new TypesNotCompatibleException(node, this, other);
	}
	
	@Override
	public Method lookupMethod(Node node, IdentifierNode id)
	        throws CompilationException
	{
		throw new NoSuchMethodException(node, this, id);
	}
	
	@Override
	public void visit(TypeVisitor visitor)
	{
	    visitor.visit(this);	    
	}
}
