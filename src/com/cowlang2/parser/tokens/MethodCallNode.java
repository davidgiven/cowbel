package com.cowlang2.parser.tokens;

import java.util.List;
import com.cowlang2.parser.core.Location;

public class MethodCallNode extends ExpressionNode
{
	public MethodCallNode(Location start, Location end, ExpressionNode object,
			IdentifierNode method, ExpressionNode... arguments)
    {
		super(start, end);
		addChild(object);
		addChild(method);
		for (ExpressionNode n : arguments)
			addChild(n);
    }
	
	public MethodCallNode(Location start, Location end, ExpressionNode object,
			IdentifierNode method, List<ExpressionNode> arguments)
	{
		super(start, end);
		addChild(object);
		addChild(method);
		for (ExpressionNode n : arguments)
			addChild(n);
	}
	
	public IdentifierNode getMethodIdentifier()
	{
		return (IdentifierNode) getChild(1);
	}
	
	@Override
	public String getShortDescription()
	{
	    return getMethodIdentifier().getText();
	}
}
