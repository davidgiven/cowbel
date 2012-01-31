/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel.ast.nodes;

import com.cowlark.cowbel.ast.HasInputs;
import com.cowlark.cowbel.ast.HasOutputs;
import com.cowlark.cowbel.ast.IsMethod;
import com.cowlark.cowbel.ast.Visitor;
import com.cowlark.cowbel.errors.CompilationException;
import com.cowlark.cowbel.methods.Method;
import com.cowlark.cowbel.parser.core.Location;

public class MethodCallStatementNode extends StatementNode
	implements IsMethod, HasInputs, HasOutputs
{
	private Method _method;
	
	public MethodCallStatementNode(Location start, Location end,
			ExpressionNode object,
			IdentifierNode method,
			TypeListNode types,
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
	
	public Method getMethod()
	{
		return _method;
	}
	
	public void setMethod(Method method)
	{
		_method = method;
	}
	
	public ExpressionNode getMethodReceiver()
	{
		return (ExpressionNode) getChild(0);
	}
	
	public IdentifierNode getMethodIdentifier()
	{
		return (IdentifierNode) getChild(1);
	}
	
	public TypeListNode getTypes()
	{
		return (TypeListNode) getChild(2);
	}
	
	public IdentifierListNode getOutputs()
	{
		return (IdentifierListNode) getChild(3);
	}
	
	public ExpressionListNode getInputs()
	{
		return (ExpressionListNode) getChild(4);
	}
	
	@Override
	public String getShortDescription()
	{
	    return getMethodIdentifier().getText();
	}
	
	@Override
	public void visit(Visitor visitor) throws CompilationException
	{
		visitor.visit(this);
	}
}
