/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING.BSD in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel.ast;

import com.cowlark.cowbel.errors.CompilationException;
import com.cowlark.cowbel.interfaces.HasIdentifier;
import com.cowlark.cowbel.interfaces.HasSymbol;
import com.cowlark.cowbel.parser.core.Location;
import com.cowlark.cowbel.symbols.Symbol;

public class ParameterDeclarationNode extends Node
		implements HasSymbol, HasIdentifier
{
	private Symbol _symbol;
	
	public ParameterDeclarationNode(Location start, Location end)
    {
		super(start, end);
    }

	public ParameterDeclarationNode(Location start, Location end,
			IdentifierNode name, AbstractTypeNode type)
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
	
	public AbstractTypeNode getVariableTypeNode()
	{
		return (AbstractTypeNode) getChild(1);
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
