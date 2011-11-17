package com.cowlang.sake.parser.tokens;

import java.util.List;
import com.cowlang.sake.parser.core.Location;

public class TypeReferenceNode extends ExpressionNode
{
	public TypeReferenceNode(Location start, Location end,
			IdentifierNode identifier, List<TypeReferenceNode> params)
    {
		super(start, end);
		
		addChild(identifier);
		for (TypeReferenceNode t : params)
			addChild(t);
    }
}
