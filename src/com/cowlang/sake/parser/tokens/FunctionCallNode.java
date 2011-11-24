package com.cowlang.sake.parser.tokens;

import java.util.List;
import com.cowlang.sake.parser.core.Location;

public class FunctionCallNode extends ExpressionNode
{
	public FunctionCallNode(Location start, Location end, ExpressionNode object,
			ExpressionNode... arguments)
    {
		super(start, end);
		addChild(object);
		for (ExpressionNode n : arguments)
			addChild(n);
    }
	
	public FunctionCallNode(Location start, Location end, ExpressionNode object,
			List<ExpressionNode> arguments)
	{
		super(start, end);
		addChild(object);
		for (ExpressionNode n : arguments)
			addChild(n);
	}
	
	public ExpressionNode getFunction()
	{
		return (ExpressionNode) getChild(0);
	}
}
