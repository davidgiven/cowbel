package com.cowlang.sake.parser.tokens;

import java.util.List;
import com.cowlang.sake.parser.core.Location;

public class ObjectInstantiationNode extends ExpressionNode
{
	public ObjectInstantiationNode(Location start, Location end)
    {
		super(start, end);
    }
	
	public ObjectInstantiationNode(Location start, Location end,
			List<ObjectInstantiationMemberNode> members)
	{
		this(start, end);
		addChildren(members);
	}
	
	@Override
	public String getShortDescription()
	{
	    return getText();
	}
}
