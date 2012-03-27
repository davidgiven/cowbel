/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING.BSD in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel.core;

import com.cowlark.cowbel.ast.AbstractScopeConstructorNode;
import com.cowlark.cowbel.ast.AbstractScopeConstructorNode.ScopeType;
import com.cowlark.cowbel.ast.BlockScopeConstructorNode;
import com.cowlark.cowbel.ast.DoWhileStatementNode;
import com.cowlark.cowbel.ast.FunctionDefinitionNode;
import com.cowlark.cowbel.ast.FunctionScopeConstructorNode;
import com.cowlark.cowbel.ast.IfElseStatementNode;
import com.cowlark.cowbel.ast.IfStatementNode;
import com.cowlark.cowbel.ast.RecursiveASTVisitor;
import com.cowlark.cowbel.ast.WhileStatementNode;
import com.cowlark.cowbel.errors.CompilationException;

public class ClassifyScopeKindsVisitor extends RecursiveASTVisitor
{
	public static ClassifyScopeKindsVisitor Instance =
		new ClassifyScopeKindsVisitor();
	
	private void make_significant_persistent(AbstractScopeConstructorNode node)
	{
		while (node.getScopeType() == ScopeType.TRIVIAL)
			node = node.getScope();
		
		node.setScopeType(ScopeType.PERSISTENT);
	}
	
	@Override
	public void visit(IfStatementNode node) throws CompilationException
	{
		node.getPositiveStatement().setScopeType(ScopeType.SIGNIFICANT);
	    super.visit(node);
	}
	
	@Override
	public void visit(IfElseStatementNode node) throws CompilationException
	{
		node.getPositiveStatement().setScopeType(ScopeType.SIGNIFICANT);
		node.getNegativeStatement().setScopeType(ScopeType.SIGNIFICANT);
	    super.visit(node);
	}
	
	@Override
	public void visit(DoWhileStatementNode node) throws CompilationException
	{
	    node.getBodyStatement().setScopeType(ScopeType.SIGNIFICANT);
	    super.visit(node);
	}
	
	@Override
	public void visit(WhileStatementNode node) throws CompilationException
	{
	    node.getBodyStatement().setScopeType(ScopeType.SIGNIFICANT);
	    super.visit(node);
	}
	
	private void visit(AbstractScopeConstructorNode node)
			throws CompilationException
	{
		/* If this scope exports any symbols to scopes in a different function,
		 * then the scope's significant scope must be persistent (or bad
		 * stuff happens). */
		
		Function thisf = node.getFunction();
		for (AbstractScopeConstructorNode n : node.getExportedScopes())
		{
			if (n.getFunction() != thisf)
			{
				make_significant_persistent(node);
				return;
			}
		}
	}
	
	@Override
	public void visit(BlockScopeConstructorNode node)
	        throws CompilationException
	{
	    visit((AbstractScopeConstructorNode) node);
	    super.visit(node);
	}
	
	@Override
	public void visit(FunctionScopeConstructorNode node)
	        throws CompilationException
	{
		node.setScopeType(ScopeType.SIGNIFICANT);
	    visit((AbstractScopeConstructorNode) node);
	    super.visit(node);
	}
	
	@Override
	public void visit(FunctionDefinitionNode node) throws CompilationException
	{
		AbstractScopeConstructorNode scope = node.getScope();
		make_significant_persistent(scope);
		/* don't recurse */
	}
}
