/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING.BSD in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel.ast;

import com.cowlark.cowbel.core.TypeRef;
import com.cowlark.cowbel.errors.CompilationException;
import com.cowlark.cowbel.interfaces.HasIdentifier;
import com.cowlark.cowbel.interfaces.HasSymbol;
import com.cowlark.cowbel.interfaces.HasTypeRef;
import com.cowlark.cowbel.parser.core.Location;
import com.cowlark.cowbel.symbols.Symbol;

public class ParameterDeclarationNode extends Node
		implements HasSymbol, HasIdentifier, HasTypeRef
{
	private Symbol _symbol;
	private TypeRef _typeref;
	
	public ParameterDeclarationNode(Location start, Location end)
    {
		super(start, end);
    }

	public ParameterDeclarationNode(Location start, Location end,
			IdentifierNode name, AbstractTypeExpressionNode type)
    {
		super(start, end);
		addChild(name);
		addChild(type);
    }

	@Override
    public IdentifierNode getIdentifier()
	{
		return (IdentifierNode) getChild(0);
	}
	
	public AbstractTypeExpressionNode getVariableTypeNode()
	{
		return (AbstractTypeExpressionNode) getChild(1);
	}
	
	@Override
	public Symbol getSymbol()
	{
	    return _symbol;
	}
	
	@Override
	public void setSymbol(Symbol symbol)
	{
	    _symbol = symbol;
	}
	
	@Override
	public TypeRef getTypeRef()
	{
		if (_typeref == null)
			setTypeRef(new TypeRef(this));
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
	public String getShortDescription()
	{
	    return getText();
	}
	
	@Override
	public void visit(ASTVisitor visitor) throws CompilationException
	{
		visitor.visit(this);
	}
}
