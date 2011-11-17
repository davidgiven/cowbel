package com.cowlang.sake.parser.tokens;

import java.util.List;
import com.cowlang.sake.parser.core.Location;

public class TypeParameterDeclarationListNode extends ExpressionNode
{
	public TypeParameterDeclarationListNode(Location start, Location end)
    {
		super(start, end);
    }
	
	public TypeParameterDeclarationListNode(Location start, Location end,
			List<IdentifierNode> identifiers)
    {
		this(start, end);
		addChildren(identifiers);
    }
	
	@Override
	public String getShortDescription()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("<");
		
		boolean first = true;
		for (IdentifierNode t : (Iterable<? extends IdentifierNode>)getChildren())
		{
			if (!first)
				sb.append(" ");
			else
				first = false;
			
			sb.append(t.getText());
		}
		
		sb.append(">");
		return sb.toString();
	}
}
