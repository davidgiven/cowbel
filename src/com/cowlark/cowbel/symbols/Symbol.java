/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING.BSD in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel.symbols;

import com.cowlark.cowbel.Constructor;
import com.cowlark.cowbel.ast.AbstractScopeConstructorNode;
import com.cowlark.cowbel.ast.IdentifierNode;
import com.cowlark.cowbel.ast.Node;
import com.cowlark.cowbel.interfaces.HasIdentifier;
import com.cowlark.cowbel.interfaces.HasNode;
import com.cowlark.cowbel.interfaces.HasScope;
import com.cowlark.cowbel.interfaces.HasType;
import com.cowlark.cowbel.types.Type;

public abstract class Symbol implements Comparable<Symbol>, HasNode, HasScope,
		HasIdentifier, HasType
{
	private static int _globalId = 0;
	
	private int _id = _globalId++;
	
	private Node _node;
	private IdentifierNode _name;
	private Type _type;
	private AbstractScopeConstructorNode _scope;
	
	public Symbol(Node node, IdentifierNode name, Type type)
	{
		_node = node;
		_name = name;
		_type = type;
	}

	@Override
    public Node getNode()
	{
		return _node;
	}
	
	@Override
    public IdentifierNode getIdentifier()
	{
		return _name;
	}
	
	@Override
    public Type getType()
	{
		return _type;
	}
	
	public void setType(Type type)
	{
		_type = type;
	}
	
	@Override
	public String toString()
	{
		return _name.getText() + ": " + _type.getCanonicalTypeName();
	}
		
	@Override
	public int compareTo(Symbol other)
	{
		if (_id < other._id)
			return -1;
		if (_id > other._id)
			return 1;
		return 0;
	}

	public void setScope(AbstractScopeConstructorNode scope)
	{
		_scope = scope;
	}
	
	@Override
    public AbstractScopeConstructorNode getScope()
	{
		return _scope;
	}
	
	public Constructor getConstructor()
	{
		if (_scope == null)
			return null;
		return _scope.getConstructor();
	}
	
	public abstract void addToConstructor(Constructor constructor);
}
