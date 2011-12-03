package com.cowlark.sake;

import com.cowlark.sake.ast.nodes.FunctionDefinitionNode;

public class Function extends Symbol
{
	public Function(FunctionDefinitionNode node)
    {
		super(node, node.getFunctionHeader().getFunctionName(),
				node.getFunctionHeader().getFunctionType());
    }
}
