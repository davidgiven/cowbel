/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING.BSD in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel.parser.parsers;

import com.cowlark.cowbel.ast.AbstractExpressionNode;
import com.cowlark.cowbel.ast.AbstractScopeConstructorNode;
import com.cowlark.cowbel.ast.BlockScopeConstructorNode;
import com.cowlark.cowbel.ast.ExpressionListNode;
import com.cowlark.cowbel.ast.IdentifierListNode;
import com.cowlark.cowbel.ast.IdentifierNode;
import com.cowlark.cowbel.ast.InferredTypeNode;
import com.cowlark.cowbel.ast.IntegerConstantNode;
import com.cowlark.cowbel.ast.MethodCallExpressionNode;
import com.cowlark.cowbel.ast.ParameterDeclarationListNode;
import com.cowlark.cowbel.ast.ParameterDeclarationNode;
import com.cowlark.cowbel.ast.StatementListNode;
import com.cowlark.cowbel.ast.TypeListNode;
import com.cowlark.cowbel.ast.VarAssignmentNode;
import com.cowlark.cowbel.ast.VarDeclarationNode;
import com.cowlark.cowbel.ast.VarReferenceNode;
import com.cowlark.cowbel.ast.WhileStatementNode;
import com.cowlark.cowbel.parser.core.Location;
import com.cowlark.cowbel.parser.core.MutableLocation;
import com.cowlark.cowbel.parser.core.ParseResult;

public class ForStatementParser extends Parser
{
	@Override
	protected ParseResult parseImpl(Location location)
	{
		ParseResult pr = ForTokenParser.parse(location);
		if (pr.failed())
			return pr;
		
		ParseResult variablepr = IdentifierParser.parse(pr.end());
		if (variablepr.failed())
			return variablepr;
		
		pr = EqualsParser.parse(variablepr.end());
		if (pr.failed())
			return pr;
		
		ParseResult initialiserpr = ExpressionLowParser.parse(pr.end());
		if (initialiserpr.failed())
			return initialiserpr;
		
		pr = CommaParser.parse(initialiserpr.end());
		if (pr.failed())
			return pr;
		
		ParseResult maximumpr = ExpressionLowParser.parse(pr.end());
		if (maximumpr.failed())
			return maximumpr;
		
		ParseResult steppr;
		Location n = maximumpr.end();
		
		pr = CommaParser.parse(maximumpr.end());
		if (pr.success())
		{
			steppr = ExpressionLowParser.parse(pr.end());
			if (steppr.failed())
				return steppr;
			
			n = steppr.end();
		}
		else
		{
			Location oneloc = new Location("1", "<internal>");
			MutableLocation oneend = new MutableLocation(oneloc);
			oneend.advance(1);

			steppr = new IntegerConstantNode(oneloc, oneend, 1);
		}
							
		ParseResult bodypr = StatementScopeConstructorParser.parse(n);
		if (bodypr.failed())
			return bodypr;
		
		Location comparisonmethodloc = new Location("!=", "<internal>");
		MutableLocation comparisonmethodend = new MutableLocation(comparisonmethodloc);
		comparisonmethodend.advance(2);

		Location incrementmethodloc = new Location("+", "<internal>");
		MutableLocation incrementmethodend = new MutableLocation(incrementmethodloc);
		incrementmethodend.advance(1);

		IdentifierNode loopcounter = IdentifierNode.createInternalIdentifier(
				"loop_counter_for_" + variablepr.getText());
		
		return new BlockScopeConstructorNode(location, bodypr.end(),
			new StatementListNode(location, pr.end(),
				new VarDeclarationNode(variablepr.start(), variablepr.end(),
					new ParameterDeclarationListNode(variablepr.start(), variablepr.end(),
						new ParameterDeclarationNode(variablepr.start(), variablepr.end(),
							loopcounter,
							new InferredTypeNode(initialiserpr.start(), initialiserpr.end())
						)
					)
				),
				new VarAssignmentNode(variablepr.start(), variablepr.end(),
					new IdentifierListNode(variablepr.start(), variablepr.end(),
						loopcounter),
					new ExpressionListNode(initialiserpr.start(), initialiserpr.end(),
						(AbstractExpressionNode) initialiserpr)
					),
				new WhileStatementNode(location, pr.end(),
					new MethodCallExpressionNode(maximumpr.start(), maximumpr.end(),
						new VarReferenceNode(variablepr.start(), variablepr.end(),
							loopcounter),
						new IdentifierNode(comparisonmethodloc, comparisonmethodend),
						new TypeListNode(comparisonmethodloc, comparisonmethodend),
						new ExpressionListNode(maximumpr.start(), maximumpr.end(),
							(AbstractExpressionNode) maximumpr)
					),
					new BlockScopeConstructorNode(bodypr.start(), bodypr.end(),
						new StatementListNode(bodypr.start(), bodypr.end(),
							new VarDeclarationNode(variablepr.start(), variablepr.end(),
								new ParameterDeclarationListNode(variablepr.start(), variablepr.end(),
									new ParameterDeclarationNode(variablepr.start(), variablepr.end(),
										(IdentifierNode) variablepr,
									new InferredTypeNode(initialiserpr.start(), initialiserpr.end())
								)
							)
						),
						new VarAssignmentNode(variablepr.start(), variablepr.end(),
							new IdentifierListNode(variablepr.start(), variablepr.end(),
								(IdentifierNode) variablepr),
							new ExpressionListNode(variablepr.start(), variablepr.end(),
								new VarReferenceNode(variablepr.start(), variablepr.end(),
									loopcounter))),
						(AbstractScopeConstructorNode) bodypr,
						new VarAssignmentNode(variablepr.start(), variablepr.end(),
						new IdentifierListNode(variablepr.start(), variablepr.end(),
							(IdentifierNode) loopcounter),
						new ExpressionListNode(maximumpr.start(), maximumpr.end(),
							new MethodCallExpressionNode(maximumpr.start(), maximumpr.end(),
								new VarReferenceNode(variablepr.start(), variablepr.end(),
									loopcounter),
								new IdentifierNode(incrementmethodloc, incrementmethodend),
								new TypeListNode(incrementmethodloc, incrementmethodend),
								new ExpressionListNode(steppr.start(), steppr.end(),
									(AbstractExpressionNode) steppr)
									)
								)
							)
						)
					)
				)
			)
		);
	}
}
