/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING.BSD in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel.core;

import com.cowlark.cowbel.ast.FunctionDefinitionNode;
import com.cowlark.cowbel.ast.MethodCallExpressionNode;
import com.cowlark.cowbel.ast.MethodCallStatementNode;
import com.cowlark.cowbel.ast.RecursiveASTVisitor;
import com.cowlark.cowbel.errors.CompilationException;
import com.cowlark.cowbel.interfaces.IsMethodCallNode;
import com.cowlark.cowbel.types.AbstractConcreteType;

public class AssignCallablesToMethods extends RecursiveASTVisitor
{
	public static AssignCallablesToMethods Instance =
		new AssignCallablesToMethods();

	@Override
	public void visit(FunctionDefinitionNode node) throws CompilationException
	{
		/* Don't recurse into nested function definitions */
	}

	private void visit(IsMethodCallNode node)
			throws CompilationException
	{
		FunctionTemplateSignature fts = new FunctionTemplateSignature(node);
		AbstractConcreteType ctype = node.getReceiver().getTypeRef().getConcreteType();
		Callable callable = ctype.getCallable(fts, node);
		node.setCallable(callable);
	}
	
	@Override
	public void visit(MethodCallExpressionNode node)
	        throws CompilationException
	{
	    super.visit(node);
	    visit((IsMethodCallNode) node);
	}
	
	@Override
	public void visit(MethodCallStatementNode node) throws CompilationException
	{
	    super.visit(node);
	    visit((IsMethodCallNode) node);
	}
}
