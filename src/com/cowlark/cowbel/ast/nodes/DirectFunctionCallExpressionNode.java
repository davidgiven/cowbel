/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel.ast.nodes;

import com.cowlark.cowbel.Function;
import com.cowlark.cowbel.ast.HasFunction;
import com.cowlark.cowbel.ast.HasIdentifier;
import com.cowlark.cowbel.ast.HasInputs;
import com.cowlark.cowbel.ast.HasSymbol;
import com.cowlark.cowbel.ast.HasTypeArguments;
import com.cowlark.cowbel.ast.Visitor;
import com.cowlark.cowbel.errors.CompilationException;
import com.cowlark.cowbel.parser.core.Location;
import com.cowlark.cowbel.symbols.Symbol;

public class DirectFunctionCallExpressionNode extends AbstractExpressionNode
		implements HasSymbol, HasIdentifier, HasInputs, HasTypeArguments,
			HasFunction
{
	private Symbol _symbol;
	private Function _function;
	
	public DirectFunctionCallExpressionNode(Location start, Location end)
    {
		super(start, end);
    }
	
	public DirectFunctionCallExpressionNode(Location start, Location end,
			IdentifierNode object,
			TypeListNode types,
			ExpressionListNode arguments)
    {
		super(start, end);
		addChild(object);
		addChild(types);
		addChild(arguments);
    }
	
	@Override
    public IdentifierNode getIdentifier()
	{
		return (IdentifierNode) getChild(0);
	}
	
	@Override
	public TypeListNode getTypeArguments()
	{
		return (TypeListNode) getChild(1);
	}
	
	@Override
    public ExpressionListNode getInputs()
	{
		return (ExpressionListNode) getChild(2);
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
	
	@Override
	public Function getFunction()
	{
	    return _function;
	}
	
	@Override
	public void setFunction(Function function)
	{
		_function = function;
	}
}
