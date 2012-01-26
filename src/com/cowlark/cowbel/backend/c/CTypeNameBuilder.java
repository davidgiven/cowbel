package com.cowlark.cowbel.backend.c;

import com.cowlark.cowbel.types.ArrayType;
import com.cowlark.cowbel.types.BooleanType;
import com.cowlark.cowbel.types.FunctionType;
import com.cowlark.cowbel.types.IntegerType;
import com.cowlark.cowbel.types.StringType;
import com.cowlark.cowbel.types.Type;
import com.cowlark.cowbel.types.TypeVisitor;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.VoidType;

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
