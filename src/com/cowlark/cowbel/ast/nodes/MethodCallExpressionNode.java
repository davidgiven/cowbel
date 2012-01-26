package com.cowlark.cowbel.ast.nodes;

import com.cowlark.cowbel.ast.IsMethodNode;
import com.cowlark.cowbel.ast.Visitor;
import com.cowlark.cowbel.errors.CompilationException;
import com.cowlark.cowbel.methods.Method;
import com.cowlark.cowbel.parser.core.Location;

public class MethodCallExpressionNode extends ExpressionNode
	implements IsMethodNode
{
	private Method _method;
	
	public MethodCallExpressionNode(Location start, Location end,
			ExpressionNode object,
			IdentifierNode method,
			ArgumentListNode arguments)
    {
		super(start, end);
		addChild(object);
		addChild(method);
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
	
	public ArgumentListNode getArguments()
	{
		return (ArgumentListNode) getChild(2);
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
