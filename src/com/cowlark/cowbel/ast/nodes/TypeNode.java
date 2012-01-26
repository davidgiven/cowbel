/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel.ast.nodes;

import com.cowlark.cowbel.ast.Visitor;
import com.cowlark.cowbel.errors.CompilationException;
import com.cowlark.cowbel.parser.core.Location;
import com.cowlark.cowbel.types.Type;

public abstract class TypeNode extends Node
{
	private Type _type;
	
	public TypeNode(Location start, Location end)
    {
        super(start, end);
    }
	
	public final Type getType()
	{
		if (_type == null)
			_type = getTypeImpl();

		return _type;
	}
	
	protected abstract Type getTypeImpl();
	
	@Override
	public void visit(Visitor visitor) throws CompilationException
	{
		visitor.visit(this);
	}
}