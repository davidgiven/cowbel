/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING.BSD in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel.symbols;

import com.cowlark.cowbel.ast.AbstractScopeConstructorNode;
import com.cowlark.cowbel.ast.IdentifierNode;
import com.cowlark.cowbel.core.Constructor;
import com.cowlark.cowbel.core.TypeRef;
import com.cowlark.cowbel.interfaces.HasIdentifier;
import com.cowlark.cowbel.interfaces.HasNode;
import com.cowlark.cowbel.interfaces.HasScope;
import com.cowlark.cowbel.interfaces.HasTypeRef;
import com.cowlark.cowbel.interfaces.IsNode;
import com.cowlark.cowbel.utils.DeterministicObject;

public abstract class Symbol extends DeterministicObject<Symbol>
		implements HasNode, HasScope, HasIdentifier, HasTypeRef
{
	private IsNode _node;
	private IdentifierNode _name;
	private TypeRef _typeref;
	private AbstractScopeConstructorNode _scope;
	
	public Symbol(IsNode node, IdentifierNode name, TypeRef typeref)
	{
		_node = node;
		_name = name;
		_typeref = typeref;
		_typeref.addUse(this);
	}

	@Override
    public IsNode getNode()
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
		if (_typeref == null)
			setTypeRef(new TypeRef(_node));
		return _typeref;
	}
	
	@Override
    public void setTypeRef(TypeRef typeref)
    {
	    _typeref = typeref;
	    typeref.addUse(this);
    }

	@Override
	public void aliasTypeRef(TypeRef tr)
	{
		if (_typeref != null)
			_typeref.alias(tr);
		else
			setTypeRef(tr);
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
