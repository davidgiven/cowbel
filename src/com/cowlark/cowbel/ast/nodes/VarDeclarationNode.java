/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel.ast.nodes;

import com.cowlark.cowbel.ast.HasSymbol;
import com.cowlark.cowbel.ast.Visitor;
import com.cowlark.cowbel.errors.CompilationException;
import com.cowlark.cowbel.parser.core.Location;
import com.cowlark.cowbel.symbols.Symbol;
import com.cowlark.cowbel.types.Type;

public class VarDeclarationNode extends StatementNode implements HasSymbol
{
	private Symbol _symbol;
	
	public VarDeclarationNode(Location start, Location end,
			IdentifierNode identifier, TypeNode type)
    {
		super(start, end);
		addChild(identifier);
		addChild(type);
    }
	
	public IdentifierNode getVariableName()
	{
		return (IdentifierNode) getChild(0);
	}
	
	public TypeNode getVariableTypeNode()
	{
		return (TypeNode) getChild(1);
	}
	
	public Type getVariableType()
	{
		return getVariableTypeNode().getType();
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
	public void visit(Visitor visitor) throws CompilationException
	{
		visitor.visit(this);
	}
}
