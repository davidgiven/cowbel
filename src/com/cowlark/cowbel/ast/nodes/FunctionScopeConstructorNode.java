/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel.ast.nodes;

import com.cowlark.cowbel.ast.Visitor;
import com.cowlark.cowbel.errors.CompilationException;
import com.cowlark.cowbel.parser.core.Location;

public class FunctionScopeConstructorNode extends AbstractScopeConstructorNode
{
	public FunctionScopeConstructorNode(Location start, Location end)
    {
        super(start, end);
    }
	
	public FunctionScopeConstructorNode(Location start, Location end, AbstractStatementNode child)
    {
        super(start, end, child);
    }

	@Override
	public boolean isFunctionScope()
	{
		return true;
	}
	
	@Override
	public FunctionScopeConstructorNode getFunctionScope()
	{
	    return this;
	}

	@Override
	public void visit(Visitor visitor) throws CompilationException
	{
		visitor.visit(this);
	}
}
