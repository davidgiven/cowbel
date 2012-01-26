/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel.ast.nodes;

import com.cowlark.cowbel.parser.core.Location;

public class FunctionScopeConstructorNode extends ScopeConstructorNode
{
	public FunctionScopeConstructorNode(Location start, Location end, StatementNode child)
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
}
