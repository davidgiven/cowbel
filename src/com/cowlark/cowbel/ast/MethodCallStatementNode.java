/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING.BSD in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel.ast;

import com.cowlark.cowbel.core.Callable;
import com.cowlark.cowbel.errors.CompilationException;
import com.cowlark.cowbel.interfaces.HasOutputs;
import com.cowlark.cowbel.interfaces.IsMethodCallNode;
import com.cowlark.cowbel.parser.core.Location;

public class MethodCallStatementNode extends AbstractStatementNode
	implements IsMethodCallNode, HasOutputs
{
	private Callable _callable;
	
	public MethodCallStatementNode(Location start, Location end)
    {
		super(start, end);
    }
	
	public MethodCallStatementNode(Location start, Location end,
			AbstractExpressionNode object,
			IdentifierNode method,
			InterfaceListNode types,
			IdentifierListNode variables,
			ExpressionListNode arguments)
    {
		super(start, end);
		addChild(object);
		addChild(method);
		addChild(types);
		addChild(variables);
		addChild(arguments);
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
	
	@Override
    public AbstractExpressionNode getReceiver()
	{
		return (AbstractExpressionNode) getChild(0);
	}
	
	@Override
    public IdentifierNode getIdentifier()
	{
		return (IdentifierNode) getChild(1);
	}
	
	@Override
	public InterfaceListNode getTypeArguments()
	{
		return (InterfaceListNode) getChild(2);
	}
	
	@Override
    public IdentifierListNode getOutputs()
	{
		return (IdentifierListNode) getChild(3);
	}
	
	@Override
    public ExpressionListNode getInputs()
	{
		return (ExpressionListNode) getChild(4);
	}
	
	@Override
	public String getShortDescription()
	{
	    return getIdentifier().getText();
	}
	
	@Override
	public void visit(ASTVisitor visitor) throws CompilationException
	{
		visitor.visit(this);
	}
}
