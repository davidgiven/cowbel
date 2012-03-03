/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING.BSD in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel;

import com.cowlark.cowbel.ast.BlockScopeConstructorNode;
import com.cowlark.cowbel.ast.FunctionDefinitionNode;
import com.cowlark.cowbel.ast.RecursiveASTVisitor;
import com.cowlark.cowbel.core.Function;
import com.cowlark.cowbel.errors.CompilationException;

public class AssignFunctionsToScopesVisitor extends RecursiveASTVisitor
{
	public static AssignFunctionsToScopesVisitor Instance =
		new AssignFunctionsToScopesVisitor();
	
	@Override
	public void visit(FunctionDefinitionNode node) throws CompilationException
	{
	}
	
	@Override
	public void visit(BlockScopeConstructorNode node)
	        throws CompilationException
	{
		Function f = node.getScope().getFunction();
		node.setFunction(f);
	    super.visit(node);
	}
}
