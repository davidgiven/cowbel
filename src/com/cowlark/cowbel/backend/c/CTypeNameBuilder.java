/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING.BSD in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel.backend.c;

import com.cowlark.cowbel.Constructor;
import com.cowlark.cowbel.ast.nodes.BlockScopeConstructorNode;
import com.cowlark.cowbel.types.ArrayType;
import com.cowlark.cowbel.types.BooleanType;
import com.cowlark.cowbel.types.ClassType;
import com.cowlark.cowbel.types.FunctionType;
import com.cowlark.cowbel.types.IntegerType;
import com.cowlark.cowbel.types.StringType;
import com.cowlark.cowbel.types.Type;
import com.cowlark.cowbel.types.TypeVisitor;

public class CTypeNameBuilder extends TypeVisitor
{
	private CBackend _backend;
	private String _type;

	public CTypeNameBuilder(CBackend backend)
    {
		_backend = backend;
    }
	
	public String buildName(Type type)
	{
		_type = null;
		type.visit(this);
		return _type;
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
	public void visit(ClassType type)
	{
		BlockScopeConstructorNode block = type.getBlock();
		Constructor constructor = block.getConstructor();
		_type = _backend.ctype(constructor);
		_type = _type + "*";
	}
	
	@Override
	public void visit(FunctionType type)
	{
		assert(false);
	}
}
