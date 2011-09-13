package com.cowlang2.parser.nodes;

import com.cowlang2.parser.core.Location;
import com.cowlang2.parser.core.ParseResult;
import com.cowlang2.parser.tokens.ExpressionNode;
import com.cowlang2.parser.tokens.MethodCallNode;
import com.cowlang2.parser.tokens.TextToken;

public class MethodCallParser extends Parser
{
	public static MethodCallParser Instance = new MethodCallParser();
	
	@Override
	protected ParseResult parseImpl(Location location)
	{
		ParseResult object = Expression3.Instance.parse(location);
		if (object.failed())
			return object;
		
		ParseResult pr = DotParser.Instance.parse(object.end());
		if (pr.failed())
			return pr;
		
		ParseResult method = MethodNameParser.Instance.parse(pr.end());
		if (method.failed())
			return method;
		
		pr = OpenParenthesisParser.Instance.parse(method.end());
		if (pr.failed())
			return pr;
		
		pr = CloseParenthesisParser.Instance.parse(pr.end());
		if (pr.failed())
			return pr;
		
		return new MethodCallNode(location, pr.end(),
				(ExpressionNode)object, (TextToken)method);
	}
}
