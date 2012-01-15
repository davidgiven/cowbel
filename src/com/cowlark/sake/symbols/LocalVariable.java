package com.cowlark.sake.symbols;

import com.cowlark.sake.ast.nodes.ParameterDeclarationNode;
import com.cowlark.sake.ast.nodes.VarDeclarationNode;

public class LocalVariable extends Variable
{
	public LocalVariable(VarDeclarationNode node)
    {
		super(node, node.getVariableName(), node.getVariableType());
    }
	
	public LocalVariable(ParameterDeclarationNode node)
	{
		super(node, node.getVariableName(), node.getVariableType());
	}
}
