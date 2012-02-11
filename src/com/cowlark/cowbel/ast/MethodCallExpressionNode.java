/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING.BSD in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel.ast;

import com.cowlark.cowbel.errors.CompilationException;
import com.cowlark.cowbel.interfaces.HasInputs;
import com.cowlark.cowbel.interfaces.HasTypeArguments;
import com.cowlark.cowbel.interfaces.IsMethod;
import com.cowlark.cowbel.methods.Method;
import com.cowlark.cowbel.parser.core.Location;

public class MethodCallExpressionNode extends AbstractExpressionNode
	implements IsMethod, HasInputs, HasTypeArguments
{
	private Method _method;
	
	public MethodCallExpressionNode(Location start, Location end)
    {
		super(start, end);
    }
	
	public MethodCallExpressionNode(Location start, Location end,
			AbstractExpressionNode object,
			IdentifierNode method,
			TypeListNode types,
			ExpressionListNode arguments)
    {
		super(start, end);
		addChild(object);
		addChild(method);
		addChild(types);
		addChild(arguments);
    }
	
	public Method getMethod()
	{
		return _method;
	}
	
	public void setMethod(Method method)
	{
		_method = method;
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
	public TypeListNode getTypeArguments()
	{
		return (TypeListNode) getChild(2);
	}
	
	@Override
    public ExpressionListNode getInputs()
	{
		return (ExpressionListNode) getChild(3);
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
