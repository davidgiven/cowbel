package com.cowlark.sake.parser.parsers;

import com.cowlark.sake.ast.nodes.FunctionHeaderNode;
import com.cowlark.sake.ast.nodes.IdentifierNode;
import com.cowlark.sake.ast.nodes.ParameterDeclarationListNode;
import com.cowlark.sake.ast.nodes.TypeNode;
import com.cowlark.sake.ast.nodes.VoidTypeNode;
import com.cowlark.sake.parser.core.Location;
import com.cowlark.sake.parser.core.ParseResult;

public class FunctionHeaderParser extends Parser
{
	@Override
	protected ParseResult parseImpl(Location location)
	{
		ParseResult pr = FunctionTokenParser.parse(location);
		if (pr.failed())
			return pr;
		
		ParseResult namepr = IdentifierParser.parse(pr.end());
		if (namepr.failed())
			return namepr;
		
		ParseResult inputargspr = ParameterDeclarationListParser.parse(namepr.end());
		if (inputargspr.failed())
			return inputargspr;
		
		pr = ColonParser.parse(inputargspr.end());
		if (pr.failed())
		{
			/* Returns void */
			
			Location n = inputargspr.end();
			return new FunctionHeaderNode(location, n,
					(IdentifierNode) namepr,
					(ParameterDeclarationListNode) inputargspr,
					new VoidTypeNode(n, n));
		}
		
		ParseResult returntypepr = TypeParser.parse(pr.end());
		if (returntypepr.failed())
			return returntypepr;
		
		return new FunctionHeaderNode(location, returntypepr.end(),
				(IdentifierNode) namepr,
				(ParameterDeclarationListNode) inputargspr,
				(TypeNode) returntypepr);
	}
}
