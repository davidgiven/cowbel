/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING.BSD in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel.ast;

import com.cowlark.cowbel.errors.CompilationException;
import com.cowlark.cowbel.parser.core.Location;
import com.cowlark.cowbel.types.Type;

public abstract class AbstractTypeNode extends Node
{
	public Type _type;
	
	public AbstractTypeNode(Location start, Location end)
    {
        super(start, end);
    }
	
	@Override
	public void visit(ASTVisitor visitor) throws CompilationException
	{
		visitor.visit(this);
	}
	
	protected abstract Type calculateType() throws CompilationException;
	
	public Type getType() throws CompilationException
    {
		if (_type == null)
			_type = calculateType();
	    return _type;
    }
	
	public void setType(Type type)
    {
	    _type = type;
    }
}