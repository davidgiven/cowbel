package com.cowlark.sake;

import com.cowlark.sake.ast.nodes.VarDeclarationNode;

public class GlobalVariable extends Variable
{
	public GlobalVariable(VarDeclarationNode node)
    {
		super(node, node.getVariableName(), node.getVariableType());
    }
}
