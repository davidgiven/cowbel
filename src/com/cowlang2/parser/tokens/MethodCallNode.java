package com.cowlang2.parser.tokens;

import com.cowlang2.parser.core.Location;

public class MethodCallNode extends ExpressionNode
{
	private TextToken _method;
	public MethodCallNode(Location start, Location end, ExpressionNode object,
			TextToken method, ExpressionNode... arguments)
    {
		super(start, end);
		addChild(object);
		for (ExpressionNode n : arguments)
			addChild(n);
		
		_method = method;
    }
	
	@Override
	public String getShortDescription()
	{
	    return _method.getText();
	}
}
