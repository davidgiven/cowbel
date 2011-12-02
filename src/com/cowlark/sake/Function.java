package com.cowlark.sake;

import com.cowlark.sake.ast.nodes.FunctionDefinitionNode;
import com.cowlark.sake.ast.nodes.IdentifierNode;

public class Function extends Symbol
{
	private FunctionDefinitionNode _node;
	
	public Function(FunctionDefinitionNode node)
    {
		super(node.getFunctionHeader().getFunctionName());
		_node = node;
    }
}
