package com.cowlark.sake.symbols;

import com.cowlark.sake.ast.nodes.ParameterDeclarationNode;
import com.cowlark.sake.ast.nodes.VarDeclarationNode;

public class LocalVariable extends Variable implements Comparable<LocalVariable>
{
	private static int _globalId = 0;
	
	private int _id = _globalId++;
	
	public LocalVariable(VarDeclarationNode node)
    {
		super(node, node.getVariableName(), node.getVariableType());
    }
	
	public LocalVariable(ParameterDeclarationNode node)
	{
		super(node, node.getVariableName(), node.getVariableType());
	}
	
	@Override
	public int compareTo(LocalVariable other)
	{
	    return Integer.compare(_id, other._id);
	}
}
