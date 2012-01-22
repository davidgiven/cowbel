package com.cowlark.sake.backend.c;

import com.cowlark.sake.types.ArrayType;
import com.cowlark.sake.types.BooleanType;
import com.cowlark.sake.types.FunctionType;
import com.cowlark.sake.types.IntegerType;
import com.cowlark.sake.types.StringType;
import com.cowlark.sake.types.Type;
import com.cowlark.sake.types.TypeVisitor;
import com.cowlark.sake.types.VoidType;

public class CTypeNameBuilder extends TypeVisitor
{
	private String _type;

	public String buildName(Type type)
	{
		_type = null;
		type.visit(this);
		return _type;
	}
	
	@Override
	public void visit(VoidType type)
	{
		_type = "void";
	}
	
	@Override
	public void visit(BooleanType type)
	{
		_type = "s_boolean_t";
	}
	
	@Override
	public void visit(IntegerType type)
	{
		_type = "s_int_t";
	}
	
	@Override
	public void visit(StringType type)
	{
		_type = "s_string_t*";
	}
	
	@Override
	public void visit(ArrayType type)
	{
		type.getChildType().visit(this);
		_type = _type + "*";
	}
	
	@Override
	public void visit(FunctionType type)
	{
		assert(false);
	}
}
