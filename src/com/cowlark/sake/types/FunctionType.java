package com.cowlark.sake.types;

import java.util.List;

public class FunctionType extends Type
{
	private List<Type> _arguments;
	private Type _returntype;
	
	public FunctionType(List<Type> arguments, Type returntype)
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
}
