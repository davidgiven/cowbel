package com.cowlark.sake.parser.parsers;

import com.cowlark.sake.ast.nodes.ExpressionNode;
import com.cowlark.sake.ast.nodes.ForStatementNode;
import com.cowlark.sake.ast.nodes.IdentifierNode;
import com.cowlark.sake.ast.nodes.InferredTypeNode;
import com.cowlark.sake.ast.nodes.IntegerConstantNode;
import com.cowlark.sake.ast.nodes.MethodCallNode;
import com.cowlark.sake.ast.nodes.ScopeConstructorNode;
import com.cowlark.sake.ast.nodes.StatementListNode;
import com.cowlark.sake.ast.nodes.VarAssignmentNode;
import com.cowlark.sake.ast.nodes.VarDeclarationNode;
import com.cowlark.sake.ast.nodes.VarReferenceNode;
import com.cowlark.sake.ast.nodes.WhileStatementNode;
import com.cowlark.sake.parser.core.Location;
import com.cowlark.sake.parser.core.MutableLocation;
import com.cowlark.sake.parser.core.ParseResult;

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
							
		ParseResult bodypr = ScopeConstructorParser.parse(n);
		if (bodypr.failed())
			return bodypr;
		
		Location comparisonmethodloc = new Location("!=", "<internal>");
		MutableLocation comparisonmethodend = new MutableLocation(comparisonmethodloc);
		comparisonmethodend.advance(2);

		Location incrementmethodloc = new Location("+", "<internal>");
		MutableLocation incrementmethodend = new MutableLocation(incrementmethodloc);
		incrementmethodend.advance(1);

		IdentifierNode loopcounter = IdentifierNode.createInternalIdentifier(
				"loop counter for " + variablepr.getText());
		
		return new ScopeConstructorNode(location, bodypr.end(),
				new StatementListNode(location, pr.end(),
						new VarDeclarationNode(variablepr.start(), variablepr.end(),
								loopcounter,
								new InferredTypeNode(initialiserpr.start(), initialiserpr.end()),
								(ExpressionNode) initialiserpr),
						new VarAssignmentNode(variablepr.start(), variablepr.end(),
								loopcounter,
								(ExpressionNode) initialiserpr),
						new WhileStatementNode(location, pr.end(),
								new MethodCallNode(maximumpr, maximumpr.end(),
										new VarReferenceNode(variablepr, variablepr.end(),
												loopcounter),
										new IdentifierNode(comparisonmethodloc, comparisonmethodend),
										(ExpressionNode) maximumpr
								),
								new ScopeConstructorNode(bodypr, bodypr.end(),
										new StatementListNode(bodypr, bodypr.end(),
												new VarDeclarationNode(location, variablepr.end(),
														(IdentifierNode) variablepr,
														new InferredTypeNode(initialiserpr.start(), initialiserpr.end()),
														(ExpressionNode) initialiserpr),
												new VarAssignmentNode(variablepr.start(), variablepr.end(),
														(IdentifierNode) variablepr,
														new VarReferenceNode(variablepr.start(), variablepr.end(),
																loopcounter)),
												(ScopeConstructorNode) bodypr,
												new VarAssignmentNode(variablepr, variablepr.end(),
														(IdentifierNode) loopcounter,
														new MethodCallNode(maximumpr, maximumpr.end(),
																new VarReferenceNode(variablepr, variablepr.end(),
																		loopcounter),
																new IdentifierNode(incrementmethodloc, incrementmethodend),
																(ExpressionNode) steppr
														)
												)
										)
								)
						)
				)
		);
	}
}
