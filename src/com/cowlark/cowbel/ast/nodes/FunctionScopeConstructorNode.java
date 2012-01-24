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
