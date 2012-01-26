/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel.symbols;

import com.cowlark.cowbel.Constructor;
import com.cowlark.cowbel.ast.nodes.IdentifierNode;
import com.cowlark.cowbel.ast.nodes.Node;
import com.cowlark.cowbel.ast.nodes.ParameterDeclarationNode;
import com.cowlark.cowbel.ast.nodes.VarDeclarationNode;
import com.cowlark.cowbel.types.Type;

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
