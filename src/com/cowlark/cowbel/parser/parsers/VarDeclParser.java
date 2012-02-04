/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING.BSD in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel.parser.parsers;

import java.util.ArrayList;
import com.cowlark.cowbel.ast.nodes.AbstractStatementNode;
import com.cowlark.cowbel.ast.nodes.IdentifierListNode;
import com.cowlark.cowbel.ast.nodes.IdentifierNode;
import com.cowlark.cowbel.ast.nodes.Node;
import com.cowlark.cowbel.ast.nodes.ParameterDeclarationListNode;
import com.cowlark.cowbel.ast.nodes.ParameterDeclarationNode;
import com.cowlark.cowbel.ast.nodes.StatementListNode;
import com.cowlark.cowbel.ast.nodes.VarDeclarationNode;
import com.cowlark.cowbel.parser.core.Location;
import com.cowlark.cowbel.parser.core.ParseResult;

public class VarDeclParser extends Parser
{
	private ParseResult parse_initialiser(IdentifierListNode identifierspr,
			Location location)
	{
		ParseResult pr2 = DirectFunctionCallStatementParser.parseWithVariableList(
				identifierspr, location);
		if (pr2.success())
			return pr2;
		
		ParseResult pr1 = MethodCallStatementParser.parseWithVariableList(
				identifierspr, location);
		if (pr1.success())
			return pr1;
		
		ParseResult pr3 = VarAssignmentParser.parseWithVariableList(
				identifierspr, location);
		if (pr3.success())
			return pr3;
		
		return combineParseErrors(pr1, pr2, pr3);
	}
	
	@Override
	protected ParseResult parseImpl(Location location)
	{
		ParseResult pr = VarTokenParser.parse(location);
		if (pr.failed())
			return pr;
		
		ParseResult variablespr = VariableDeclarationListParser.parse(pr.end());
		if (variablespr.failed())
			return variablespr;
		
		/* Fake up an IdentifierListNode based on the contents of the
		 * ParameterDeclarationListNode we got above. */
		
		ParameterDeclarationListNode pdln = (ParameterDeclarationListNode) variablespr;
		ArrayList<IdentifierNode> identifiers = new ArrayList<IdentifierNode>();
		for (Node n : pdln)
		{
			ParameterDeclarationNode pdn = (ParameterDeclarationNode) n;
			identifiers.add(pdn.getVariableName());
		}
		
		IdentifierListNode identifierspr = new IdentifierListNode(
				variablespr, variablespr.end(), identifiers);
		
		ParseResult initialiserpr = parse_initialiser(identifierspr, variablespr.end());
		if (initialiserpr.failed())
			return initialiserpr;
		
		return new StatementListNode(location, initialiserpr.end(),
				new VarDeclarationNode(pdln, pdln.end(),
						pdln),
				(AbstractStatementNode) initialiserpr);
	}
}
