package com.cowlang2.parser.tokens;

import java.util.List;
import com.cowlang2.parser.core.Location;
import com.cowlang2.parser.core.ParseResult;

public class TypexDeclarationNode extends ExpressionNode
{
	private List<TextToken> _identifiers;
	
	public TypexDeclarationNode(Location start, Location end,
			List<TextToken> identifiers)
    {
		super(start, end);
		_identifiers = identifiers;
    }
	
	@Override
	public String getShortDescription()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("<");
		
		boolean first = true;
		for (TextToken t : _identifiers)
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
