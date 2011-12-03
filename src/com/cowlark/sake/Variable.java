package com.cowlark.sake;

import com.cowlark.sake.ast.nodes.IdentifierNode;
import com.cowlark.sake.ast.nodes.Node;
import com.cowlark.sake.types.Type;

public abstract class Variable extends Symbol
{
	public Variable(Node node, IdentifierNode name, Type type)
    {
		super(node, name, type);
    }
}
