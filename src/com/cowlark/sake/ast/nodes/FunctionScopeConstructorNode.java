package com.cowlark.sake.ast.nodes;

import com.cowlark.sake.parser.core.Location;

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
