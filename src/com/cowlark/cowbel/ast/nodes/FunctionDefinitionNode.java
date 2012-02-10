/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING.BSD in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel.ast.nodes;

import com.cowlark.cowbel.ast.HasIdentifier;
import com.cowlark.cowbel.ast.HasSymbol;
import com.cowlark.cowbel.ast.Visitor;
import com.cowlark.cowbel.errors.CompilationException;
import com.cowlark.cowbel.parser.core.Location;
import com.cowlark.cowbel.symbols.Symbol;

public class FunctionDefinitionNode extends AbstractStatementNode implements
		HasSymbol, HasIdentifier
{
	private Symbol _symbol;
	
	public FunctionDefinitionNode(Location start, Location end)
    {
		super(start, end);
    }
	
	public FunctionDefinitionNode(Location start, Location end,
			FunctionHeaderNode header, FunctionScopeConstructorNode body)
    {
		super(start, end);
		addChild(header);
		addChild(body);
    }	
	
	@Override
	public void visit(Visitor visitor) throws CompilationException
	{
		visitor.visit(this);
	}
	
	public FunctionHeaderNode getFunctionHeader()
	{
		return (FunctionHeaderNode) getChild(0);
	}
	
	public FunctionScopeConstructorNode getFunctionBody()
	{
		return (FunctionScopeConstructorNode) getChild(1);
	}
	
	@Override
	public IdentifierNode getIdentifier()
	{
	    return getFunctionHeader().getFunctionName();
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
}