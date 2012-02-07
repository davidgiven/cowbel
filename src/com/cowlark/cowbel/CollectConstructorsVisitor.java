/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING.BSD in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel;

import java.util.Set;
import com.cowlark.cowbel.ast.RecursiveVisitor;
import com.cowlark.cowbel.ast.nodes.BlockScopeConstructorNode;
import com.cowlark.cowbel.ast.nodes.FunctionDefinitionNode;
import com.cowlark.cowbel.ast.nodes.FunctionScopeConstructorNode;
import com.cowlark.cowbel.errors.CompilationException;

public class CollectConstructorsVisitor extends RecursiveVisitor
{
	private Set<Constructor> _constructors;
	
	public CollectConstructorsVisitor(Set<Constructor> constructors)
    {
		_constructors = constructors;
    }
	
	@Override
	public void visit(FunctionDefinitionNode node) throws CompilationException
	{
		/* don't recurse */
	}

	@Override
	public void visit(BlockScopeConstructorNode node) throws CompilationException
	{
		_constructors.add(node.getConstructor());
	    super.visit(node);
	}
	
	@Override
    public void visit(FunctionScopeConstructorNode node) throws CompilationException
	{
		_constructors.add(node.getConstructor());
		super.visit(node);
	};
}
