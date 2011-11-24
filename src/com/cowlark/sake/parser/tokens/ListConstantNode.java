package com.cowlark.sake.parser.tokens;

import java.util.List;
import com.cowlark.sake.parser.core.Location;

public class ListConstantNode extends ExpressionNode
{
	public ListConstantNode(Location start, Location end)
    {
		super(start, end);
    }
	
	public ListConstantNode(Location start, Location end,
			List<StringConstantNode> params)
    {
		this(start, end);
		addChildren(params);
    }
	
	@Override
	public String getShortDescription()
	{
	    return getText();
	}
}
