/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING.BSD in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel.symbols;

import com.cowlark.cowbel.ast.AbstractScopeConstructorNode;
import com.cowlark.cowbel.ast.IdentifierNode;
import com.cowlark.cowbel.ast.Node;
import com.cowlark.cowbel.core.Constructor;
import com.cowlark.cowbel.core.TypeRef;
import com.cowlark.cowbel.interfaces.HasIdentifier;
import com.cowlark.cowbel.interfaces.HasNode;
import com.cowlark.cowbel.interfaces.HasScope;
import com.cowlark.cowbel.interfaces.HasTypeRef;
import com.cowlark.cowbel.utils.DeterministicObject;

public abstract class Symbol extends DeterministicObject<Symbol>
		implements HasNode, HasScope, HasIdentifier, HasTypeRef
{
	private Node _node;
	private IdentifierNode _name;
	private TypeRef _typeref;
	private AbstractScopeConstructorNode _scope;
	
	public Symbol(Node node, IdentifierNode name, TypeRef typeref)
	{
		_node = node;
		_name = name;
		_typeref = typeref;
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
	public TypeRef getTypeRef()
	{
	    return _typeref;
	}
	
	@Override
	public String toString()
	{
		return _name.getText() + ": " + _typeref.toString();
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
