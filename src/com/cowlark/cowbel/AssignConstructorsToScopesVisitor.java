/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING.BSD in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel;

import java.util.Set;
import com.cowlark.cowbel.ast.RecursiveVisitor;
import com.cowlark.cowbel.ast.nodes.AbstractScopeConstructorNode;
import com.cowlark.cowbel.ast.nodes.BlockScopeConstructorNode;
import com.cowlark.cowbel.ast.nodes.DoWhileStatementNode;
import com.cowlark.cowbel.ast.nodes.FunctionDefinitionNode;
import com.cowlark.cowbel.ast.nodes.FunctionScopeConstructorNode;
import com.cowlark.cowbel.ast.nodes.WhileStatementNode;
import com.cowlark.cowbel.errors.CompilationException;

public class AssignConstructorsToScopesVisitor extends RecursiveVisitor
{
	private Set<Constructor> _constructors;
	
	public AssignConstructorsToScopesVisitor(Set<Constructor> constructors)
    {
		_constructors = constructors;
    }
	
	private void assign_stackframe(AbstractScopeConstructorNode node,
			boolean persistent)
	{
		if (node.getConstructor() != null)
			return;
		
		Constructor sf = new Constructor(node);
		_constructors.add(sf);
		node.setConstructor(sf);
		
		if (persistent)
			sf.setPersistent(true);
	}
	
	private void inherit_stackframe(AbstractScopeConstructorNode node)
	{
		if (node.getConstructor() != null)
			return;
		
		Constructor sf = node.getScope().getConstructor();
		node.setConstructor(sf);
	}
	
	private boolean is_complex_scope(AbstractScopeConstructorNode node)
	{
		/* If this scope exports *any* functions, it's complex. */
		
		/* If this scope is exporting to a scope that's part of a different
		 * function, it's complex. */
		
		Function thisfunction = node.getFunction();
		for (AbstractScopeConstructorNode s : node.getExportedScopes())
		{
			Function f = s.getFunction();
			if (f != thisfunction)
				return true;
		}
		
		return false;
	}

	@Override
	public void visit(WhileStatementNode node) throws CompilationException
	{
		AbstractScopeConstructorNode body = node.getBodyStatement();
		if (is_complex_scope(body))
			assign_stackframe(body, true);
		
	    super.visit(node);
	}
	
	@Override
	public void visit(DoWhileStatementNode node) throws CompilationException
	{
		AbstractScopeConstructorNode body = node.getBodyStatement();
		if (is_complex_scope(body))
			assign_stackframe(body, true);
		
	    super.visit(node);
	}
	
	@Override
	public void visit(BlockScopeConstructorNode node) throws CompilationException
	{
		if (!node.getLabels().isEmpty() && is_complex_scope(node))
			assign_stackframe(node, true);
		else
			inherit_stackframe(node);
		
		super.visit(node);
	}
	
	@Override
	public void visit(FunctionScopeConstructorNode node)
	        throws CompilationException
	{
		assign_stackframe(node, is_complex_scope(node));
	    super.visit(node);
	}

	@Override
	public void visit(FunctionDefinitionNode node) throws CompilationException
	{
	}
}
