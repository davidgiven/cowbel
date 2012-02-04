/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING.BSD in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel;

import com.cowlark.cowbel.ast.HasFunction;
import com.cowlark.cowbel.ast.HasIdentifier;
import com.cowlark.cowbel.ast.HasInputs;
import com.cowlark.cowbel.ast.HasOutputs;
import com.cowlark.cowbel.ast.HasSymbol;
import com.cowlark.cowbel.ast.HasTypeArguments;
import com.cowlark.cowbel.ast.RecursiveVisitor;
import com.cowlark.cowbel.ast.nodes.AbstractScopeConstructorNode;
import com.cowlark.cowbel.ast.nodes.DirectFunctionCallExpressionNode;
import com.cowlark.cowbel.ast.nodes.DirectFunctionCallStatementNode;
import com.cowlark.cowbel.ast.nodes.ExpressionListNode;
import com.cowlark.cowbel.ast.nodes.FunctionDefinitionNode;
import com.cowlark.cowbel.ast.nodes.GotoStatementNode;
import com.cowlark.cowbel.ast.nodes.IdentifierListNode;
import com.cowlark.cowbel.ast.nodes.IdentifierNode;
import com.cowlark.cowbel.ast.nodes.MethodCallStatementNode;
import com.cowlark.cowbel.ast.nodes.Node;
import com.cowlark.cowbel.ast.nodes.VarAssignmentNode;
import com.cowlark.cowbel.ast.nodes.VarReferenceNode;
import com.cowlark.cowbel.errors.CompilationException;
import com.cowlark.cowbel.errors.WrongNumberOfExpressionsInMultipleAssignments;
import com.cowlark.cowbel.symbols.Symbol;

public class ResolveVariableReferencesVisitor extends RecursiveVisitor
{
	@Override
	public void visit(FunctionDefinitionNode node) throws CompilationException
	{
		/* Don't recurse into nested functions (they'll be handled later). */
	}
	
	@Override
	public void visit(GotoStatementNode node) throws CompilationException
	{
		AbstractScopeConstructorNode scope = node.getScope();
		IdentifierNode in = node.getLabelName();
		Label label = scope.lookupLabel(in);
		
		node.setLabel(label);
	    super.visit(node);
	}
	
	private <T extends Node & HasInputs & HasTypeArguments & HasIdentifier
			& HasSymbol & HasFunction>
		void direct_function_call(T node) throws CompilationException
	{
		AbstractScopeConstructorNode scope = node.getScope();
		IdentifierNode in = node.getIdentifier();
		
		/* Try to look this up as a function first. */
		
		Function function = scope.lookupFunction(node);
		if (function != null)
		{
			node.setFunction(function);
			scope.importFunction(function);
			return;
		}
		
		/* If that failed, treat it as a variable reference (doing an
		 * indirect function call via a delegate). */
		
		Symbol symbol = scope.lookupVariable(in);
		node.setSymbol(symbol);
		scope.importSymbol(symbol);
	}
	
	private <T extends Node & HasOutputs>
		void direct_call(T node) throws CompilationException
	{
		IdentifierListNode variables = node.getOutputs();
		
		AbstractScopeConstructorNode scope = node.getScope();
		
		for (int i = 0; i < variables.getNumberOfChildren(); i++)
		{
			IdentifierNode id = variables.getIdentifier(i);
			Symbol symbol = scope.lookupVariable(id);
			
			scope.importSymbol(symbol);
			variables.setSymbol(i, symbol);
		}
	}
	
	@Override
	public void visit(DirectFunctionCallExpressionNode node) throws CompilationException
	{
		direct_function_call(node);
	    super.visit(node);
	}
	
	@Override
	public void visit(DirectFunctionCallStatementNode node) throws CompilationException
	{
		direct_function_call(node);
		direct_call(node);
	    super.visit(node);
	}
	
	@Override
	public void visit(MethodCallStatementNode node) throws CompilationException
	{
		direct_call(node);
	    super.visit(node);
	}
	
	@Override
	public void visit(VarReferenceNode node)
	        throws CompilationException
	{
		AbstractScopeConstructorNode scope = node.getScope();
		IdentifierNode in = node.getVariableName();
		Symbol symbol = scope.lookupVariable(in);
		
		if (scope != symbol.getScope())
			scope.importSymbol(symbol);
		
		node.setSymbol(symbol);
	    super.visit(node);
	}
	
	@Override
	public void visit(VarAssignmentNode node)
	        throws CompilationException
	{
		AbstractScopeConstructorNode scope = node.getScope();
		IdentifierListNode iln = node.getVariables();
		ExpressionListNode eln = node.getExpressions();
		
		if (iln.getNumberOfChildren() != eln.getNumberOfChildren())
			throw new WrongNumberOfExpressionsInMultipleAssignments(node);
		
		for (int i = 0; i < iln.getNumberOfChildren(); i++)
		{
			IdentifierNode in = iln.getIdentifier(i);
			Symbol symbol = scope.lookupVariable(in);
			
			if (scope != symbol.getScope())
				scope.importSymbol(symbol);
			
			iln.setSymbol(i, symbol);
		}
		
	    super.visit(node);
	}
}
