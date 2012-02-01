/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel.ast.nodes;

import com.cowlark.cowbel.ast.HasIdentifier;
import com.cowlark.cowbel.ast.HasInputs;
import com.cowlark.cowbel.ast.HasOutputs;
import com.cowlark.cowbel.ast.HasSymbol;
import com.cowlark.cowbel.ast.Visitor;
import com.cowlark.cowbel.errors.CompilationException;
import com.cowlark.cowbel.parser.core.Location;
import com.cowlark.cowbel.symbols.Symbol;

public class DirectFunctionCallStatementNode extends StatementNode
		implements HasSymbol, HasIdentifier, HasInputs, HasOutputs
{
	private Symbol _symbol;
	
	public DirectFunctionCallStatementNode(Location start, Location end,
			IdentifierNode object, TypeListNode types,
			IdentifierListNode variables, ExpressionListNode arguments)
    {
		super(start, end);
		addChild(object);
		addChild(types);
		addChild(variables);
		addChild(arguments);
    }
	
	@Override
    public IdentifierNode getIdentifier()
	{
		return (IdentifierNode) getChild(0);
	}

	public TypeListNode getTypes()
	{
		return (TypeListNode) getChild(1);
	}
	
	@Override
    public IdentifierListNode getOutputs()
	{
		return (IdentifierListNode) getChild(2);
	}
	
	@Override
    public ExpressionListNode getInputs()
	{
		return (ExpressionListNode) getChild(3);
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
