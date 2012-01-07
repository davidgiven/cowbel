package com.cowlark.sake.symbols;

import com.cowlark.sake.ast.nodes.IdentifierNode;
import com.cowlark.sake.ast.nodes.Node;
import com.cowlark.sake.ast.nodes.VarDeclarationNode;
import com.cowlark.sake.types.Type;

public class GlobalVariable extends Variable
{
	public GlobalVariable(VarDeclarationNode node)
    {
		super(node, node.getVariableName(), node.getVariableType());
    }
	
	public GlobalVariable(Node node, IdentifierNode name, Type type)
    {
		super(node, name, type);
    }
}
