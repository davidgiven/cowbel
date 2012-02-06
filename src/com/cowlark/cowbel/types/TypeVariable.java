/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING.BSD in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel.types;

import com.cowlark.cowbel.ast.HasInputs;
import com.cowlark.cowbel.ast.HasTypeArguments;
import com.cowlark.cowbel.ast.IsMethod;
import com.cowlark.cowbel.ast.nodes.IdentifierNode;
import com.cowlark.cowbel.ast.nodes.Node;
import com.cowlark.cowbel.errors.CompilationException;
import com.cowlark.cowbel.methods.Method;

public class TypeVariable extends Type
{
	public static TypeVariable create(IdentifierNode identifier)
	{
		return new TypeVariable(identifier);
	}
	
	private IdentifierNode _identifier;
	
	private TypeVariable(IdentifierNode identifier)
    {
		_identifier = identifier;
    }
	
	@Override
	public boolean isVoidType()
	{
		return getRealType().isVoidType();
	}
	
	@Override
	public Type getRealType()
	{
		assert(false);
		return null;
	}
	
	@Override
	public String getCanonicalTypeName()
	{
		assert(false);
		return null;
	}

	@Override
	public boolean isConcreteType()
	{
		assert(false);
		return false;
	}

	@Override
	public void unifyWithImpl(Node node, Type other) throws CompilationException
	{
		assert(false);
	}
	
	@Override
	public <T extends Node & IsMethod & HasInputs & HasTypeArguments>
		Method lookupMethod(
	        T node, IdentifierNode id) throws CompilationException
	{
		assert(false);
		return null;
	}
	
	@Override
	public void visit(TypeVisitor visitor)
	{
		assert(false);
	}
}
