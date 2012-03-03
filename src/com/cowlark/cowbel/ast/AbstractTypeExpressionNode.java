/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING.BSD in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel.ast;

import com.cowlark.cowbel.core.Interface;
import com.cowlark.cowbel.errors.CompilationException;
import com.cowlark.cowbel.parser.core.Location;

public abstract class AbstractTypeExpressionNode extends Node
{
	public Interface _type;
	
	public AbstractTypeExpressionNode(Location start, Location end)
    {
        super(start, end);
    }
	
	@Override
	public void visit(ASTVisitor visitor) throws CompilationException
	{
		visitor.visit(this);
	}

	public Interface getInterface() throws CompilationException
	{
		if (_type == null)
			_type = createInterface();
		return _type;
	}
	
	abstract public Interface createInterface() throws CompilationException;
}