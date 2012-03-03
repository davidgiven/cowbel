/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING.BSD in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel.ast;

import com.cowlark.cowbel.core.TypeRef;
import com.cowlark.cowbel.errors.CompilationException;
import com.cowlark.cowbel.interfaces.IsExpressionNode;
import com.cowlark.cowbel.parser.core.Location;

public abstract class AbstractExpressionNode extends Node
		implements IsExpressionNode
{
	private TypeRef _typeref = null;
	
	public AbstractExpressionNode(Location start, Location end)
    {
        super(start, end);
    }
	
	@Override
	public void visit(ASTVisitor visitor) throws CompilationException
	{
		visitor.visit(this);
	}
	
	public boolean hasTypeRef()
	{
		return (_typeref != null);
	}
	
	@Override
	public TypeRef getTypeRef()
	{
		if (_typeref == null)
			_typeref = new TypeRef(this);
		return _typeref;
	}
}