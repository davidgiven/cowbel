package com.cowlark.sake;

import com.cowlark.sake.ast.nodes.IdentifierNode;

public abstract class Variable extends Symbol
{
	public Variable(IdentifierNode name)
    {
		super(name);
    }
}
