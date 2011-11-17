package com.cowlang.sake.parser.nodes;

import java.util.ArrayList;
import com.cowlang.sake.parser.core.Location;
import com.cowlang.sake.parser.core.ParseResult;
import com.cowlang.sake.parser.tokens.IdentifierNode;
import com.cowlang.sake.parser.tokens.MethodHeaderNode;
import com.cowlang.sake.parser.tokens.ParameterDeclarationListNode;
import com.cowlang.sake.parser.tokens.TypeParameterDeclarationListNode;

public class MethodHeaderParser extends Parser
{
	@Override
	protected ParseResult parseImpl(Location location)
	{
		ParseResult pr = MethodTokenParser.parse(location);
		if (pr.failed())
			return pr;
		
		ParseResult namepr = MethodNameParser.parse(pr.end());
		if (namepr.failed())
			return namepr;
		
		Location n = namepr.end();
		ParseResult typexpr = TypexDeclarationParser.parse(n);
		if (typexpr.success())
			n = typexpr.end();
		else
			typexpr = new TypeParameterDeclarationListNode(n, n,
					new ArrayList<IdentifierNode>());
		
		ParseResult inputargspr = ParameterDeclarationListParser.parse(n);
		if (inputargspr.failed())
			return inputargspr;
		
		pr = ColonParser.parse(inputargspr.end());
		if (pr.failed())
		{
			/* No output arguments */
			
			n = inputargspr.end();
			return new MethodHeaderNode(location, n,
					(IdentifierNode) namepr, (TypeParameterDeclarationListNode) typexpr,
					(ParameterDeclarationListNode) inputargspr,
					new ParameterDeclarationListNode(n, n));
		}
		
		ParseResult outputargspr = ParameterDeclarationListParser.parse(pr.end());
		if (outputargspr.failed())
			return outputargspr;
		
		return new MethodHeaderNode(location, outputargspr.end(),
				(IdentifierNode) namepr, (TypeParameterDeclarationListNode) typexpr,
				(ParameterDeclarationListNode) inputargspr,
				(ParameterDeclarationListNode) outputargspr);
	}
}
