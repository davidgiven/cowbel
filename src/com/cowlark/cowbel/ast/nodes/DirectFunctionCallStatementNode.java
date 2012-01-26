/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel.ast.nodes;

import com.cowlark.cowbel.ast.HasIdentifier;
import com.cowlark.cowbel.ast.HasSymbol;
import com.cowlark.cowbel.ast.IsCallableStatement;
import com.cowlark.cowbel.ast.Visitor;
import com.cowlark.cowbel.errors.CompilationException;
import com.cowlark.cowbel.parser.core.Location;
import com.cowlark.cowbel.symbols.Symbol;

public class DirectFunctionCallStatementNode extends StatementNode
		implements HasSymbol, IsCallableStatement, HasIdentifier
{
	private Symbol _symbol;
	
	public DirectFunctionCallStatementNode(Location start, Location end,
			IdentifierNode object,
			IdentifierListNode variables, ArgumentListNode arguments)
    {
		super(start, end);
		addChild(object);
		addChild(variables);
		addChild(arguments);
    }
	
	public IdentifierNode getIdentifier()
	{
		return (IdentifierNode) getChild(0);
	}

	public IdentifierListNode getVariables()
	{
		return (IdentifierListNode) getChild(1);
	}
	
	public ArgumentListNode getArguments()
	{
		return (ArgumentListNode) getChild(2);
	}

	@Override
	public void visit(Visitor visitor) throws CompilationException
	{
		visitor.visit(this);
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
