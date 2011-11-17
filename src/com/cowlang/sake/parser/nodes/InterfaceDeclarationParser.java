package com.cowlang.sake.parser.nodes;

import java.util.ArrayList;
import com.cowlang.sake.parser.core.Location;
import com.cowlang.sake.parser.core.ParseResult;
import com.cowlang.sake.parser.tokens.IdentifierNode;
import com.cowlang.sake.parser.tokens.InterfaceDeclarationNode;
import com.cowlang.sake.parser.tokens.MethodDeclarationNode;
import com.cowlang.sake.parser.tokens.TypeParameterDeclarationListNode;

public class InterfaceDeclarationParser extends Parser
{
	@Override
	protected ParseResult parseImpl(Location location)
	{
		ParseResult pr1 = InterfaceTokenParser.parse(location);
		if (pr1.failed())
			return pr1;
		
		ParseResult identifierpr = IdentifierParser.parse(pr1.end());
		if (identifierpr.failed())
			return identifierpr;
		
		Location n = identifierpr.end();
		ParseResult typexpr = TypexDeclarationParser.parse(n);
		if (typexpr.success())
			n = typexpr.end();
		else
			typexpr = new TypeParameterDeclarationListNode(n, n);
		
		ParseResult pr2 = OpenBraceParser.parse(n);
		if (pr2.failed())
			return combineParseErrors(identifierpr, pr2);
		
		ArrayList<MethodDeclarationNode> methods = new ArrayList<MethodDeclarationNode>();
		
		n = pr2.end();
		for (;;)
		{
			ParseResult pr3 = CloseBraceParser.parse(n);
			if (pr3.success())
			{
				n = pr3.end();
				break;
			}
		
			ParseResult pr4 = MethodDeclarationParser.parse(pr3.end());
			if (pr4.failed())
				return pr4;
			
			methods.add((MethodDeclarationNode) pr4);
			n = pr4.end();
		}
		
		return new InterfaceDeclarationNode(location, n,
				(IdentifierNode) identifierpr,
				(TypeParameterDeclarationListNode) typexpr,
				methods);
	}
}
