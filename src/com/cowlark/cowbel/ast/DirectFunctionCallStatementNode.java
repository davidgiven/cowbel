/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING.BSD in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel.ast;

import com.cowlark.cowbel.core.Callable;
import com.cowlark.cowbel.errors.CompilationException;
import com.cowlark.cowbel.interfaces.HasOutputs;
import com.cowlark.cowbel.interfaces.IsFunctionCallNode;
import com.cowlark.cowbel.parser.core.Location;
import com.cowlark.cowbel.symbols.Symbol;

public class DirectFunctionCallStatementNode extends AbstractStatementNode
		implements IsFunctionCallNode, HasOutputs
{
	private Symbol _symbol;
	private Callable _callable;
	
	public DirectFunctionCallStatementNode(Location start, Location end)
	{
		super(start, end);
	}
	
	public DirectFunctionCallStatementNode(Location start, Location end,
			IdentifierNode object, InterfaceListNode types,
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

	@Override
	public InterfaceListNode getTypeArguments()
	{
		return (InterfaceListNode) getChild(1);
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
	public void visit(ASTVisitor visitor) throws CompilationException
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
	public Callable getCallable()
	{
	    return _callable;
	}
	
	@Override
	public void setCallable(Callable callable)
	{
		_callable = callable;
	}
}
