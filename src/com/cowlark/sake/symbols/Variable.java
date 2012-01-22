package com.cowlark.sake.symbols;

import com.cowlark.sake.Constructor;
import com.cowlark.sake.ast.nodes.IdentifierNode;
import com.cowlark.sake.ast.nodes.Node;
import com.cowlark.sake.ast.nodes.ParameterDeclarationNode;
import com.cowlark.sake.ast.nodes.VarDeclarationNode;
import com.cowlark.sake.types.Type;

public class Variable extends Symbol
{
	private boolean _isParameter = false;
	
	public Variable(Node node, IdentifierNode name, Type type)
	{
		super(node, name, type);
	}

	public Variable(VarDeclarationNode node)
    {
		super(node, node.getVariableName(), node.getVariableType());
    }
	
	public Variable(ParameterDeclarationNode node)
	{
		super(node, node.getVariableName(), node.getVariableType());
	}
	
	@Override
	public String getMangledName()
	{
		return getName();
	}
	
	@Override
	public boolean collidesWith(Symbol other)
	{
		return getName().equals(other.getName());
	}

	public boolean isParameter()
    {
	    return _isParameter;
    }
	
	public void setParameter(boolean isParameter)
    {
	    _isParameter = isParameter;
    }
	
	@Override
	public void addToConstructor(Constructor constructor)
	{
		constructor.addVariable(this);
	}
}
