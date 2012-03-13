/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING.BSD in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel.core;

import com.cowlark.cowbel.ast.AbstractScopeConstructorNode;
import com.cowlark.cowbel.ast.DirectFunctionCallExpressionNode;
import com.cowlark.cowbel.ast.DirectFunctionCallStatementNode;
import com.cowlark.cowbel.ast.ExpressionListNode;
import com.cowlark.cowbel.ast.FunctionDefinitionNode;
import com.cowlark.cowbel.ast.GotoStatementNode;
import com.cowlark.cowbel.ast.IdentifierListNode;
import com.cowlark.cowbel.ast.IdentifierNode;
import com.cowlark.cowbel.ast.MethodCallStatementNode;
import com.cowlark.cowbel.ast.Node;
import com.cowlark.cowbel.ast.RecursiveASTVisitor;
import com.cowlark.cowbel.ast.VarAssignmentNode;
import com.cowlark.cowbel.ast.VarReferenceNode;
import com.cowlark.cowbel.errors.CompilationException;
import com.cowlark.cowbel.errors.CouldNotFindMethod;
import com.cowlark.cowbel.errors.IdentifierNotFound;
import com.cowlark.cowbel.errors.WrongNumberOfExpressionsInMultipleAssignments;
import com.cowlark.cowbel.interfaces.HasOutputs;
import com.cowlark.cowbel.interfaces.IsFunctionCallNode;
import com.cowlark.cowbel.symbols.Symbol;

public class ResolveVariableReferencesVisitor extends RecursiveASTVisitor
{
	public static ResolveVariableReferencesVisitor Instance =
		new ResolveVariableReferencesVisitor();
	
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
	
	private void direct_function_call(IsFunctionCallNode node) throws CompilationException
	{
		AbstractScopeConstructorNode scope = node.getScope();
		IdentifierNode in = node.getIdentifier();
		
		/* Try to look this up as a function first. */
		
		Callable callable = scope.lookupFunction(node);
		if (callable != null)
		{
			node.setCallable(callable);
			scope.importScope(callable.getNode().getScope());
			return;
		}
		
		/* If that failed, treat it as a variable reference (doing an
		 * indirect function call via a delegate). */
		
		throw new IdentifierNotFound(scope, node.getIdentifier());
//		Symbol symbol = scope.lookupVariable(in);
//		node.setSymbol(symbol);
//		scope.importSymbol(symbol);
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
