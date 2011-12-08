package com.cowlark.sake.types;

import java.util.LinkedList;
import java.util.List;

public class FunctionType extends Type
{
	public static FunctionType create(List<Type> arguments, Type returntype)
	{
		FunctionType type = new FunctionType(arguments, returntype);
		return TypeRegistry.canonicalise(type);
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
}
