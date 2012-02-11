/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING.BSD in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel.parser.parsers;

import com.cowlark.cowbel.ast.FunctionHeaderNode;
import com.cowlark.cowbel.ast.IdentifierListNode;
import com.cowlark.cowbel.ast.IdentifierNode;
import com.cowlark.cowbel.ast.ParameterDeclarationListNode;
import com.cowlark.cowbel.ast.ParameterDeclarationNode;
import com.cowlark.cowbel.ast.TypeVariableNode;
import com.cowlark.cowbel.parser.core.Location;
import com.cowlark.cowbel.parser.core.ParseResult;

public class FunctionHeaderParser extends Parser
{
	@Override
	protected ParseResult parseImpl(Location location)
	{
		ParseResult pr = FunctionTokenParser.parse(location);
		if (pr.failed())
			return pr;
		
		ParseResult namepr = MethodNameParser.parse(pr.end());
		if (namepr.failed())
			return namepr;
		
		ParseResult typevarspr = TypeVariableListParser.parse(namepr.end());
		if (typevarspr.failed())
			return typevarspr;
		
		ParseResult inputargspr = ParameterDeclarationListParser.parse(typevarspr.end());
		if (inputargspr.failed())
			return inputargspr;
		
		pr = ColonParser.parse(inputargspr.end());
		if (pr.failed())
		{
			/* Returns void */
			
			Location n = inputargspr.end();
			return new FunctionHeaderNode(location, n,
					(IdentifierNode) namepr,
					(IdentifierListNode) typevarspr,
					(ParameterDeclarationListNode) inputargspr,
					new ParameterDeclarationListNode(pr.start(), pr.start()));
		}
		
		ParseResult outputargspr;
		
		ParseResult returntypepr = TypeParser.parse(pr.end());
		if (returntypepr.success())
		{
			outputargspr =
				new ParameterDeclarationListNode(
					returntypepr.start(), returntypepr.end(),
					new ParameterDeclarationNode(
							returntypepr.start(), returntypepr.end(),
							IdentifierNode.createInternalIdentifier("return"),
							(TypeVariableNode) returntypepr
						)
				);
		}
		else
		{
			outputargspr = ParameterDeclarationListParser.parse(pr.end());
			if (outputargspr.failed())
				return outputargspr;
		}

		return new FunctionHeaderNode(location, outputargspr.end(),
				(IdentifierNode) namepr,
				(IdentifierListNode) typevarspr,
				(ParameterDeclarationListNode) inputargspr,
				(ParameterDeclarationListNode) outputargspr);
	}
}
