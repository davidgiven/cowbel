/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING.BSD in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel;

import com.cowlark.cowbel.ast.RecursiveVisitor;
import com.cowlark.cowbel.ast.nodes.AbstractScopeConstructorNode;
import com.cowlark.cowbel.ast.nodes.BlockScopeConstructorNode;
import com.cowlark.cowbel.ast.nodes.FunctionDefinitionNode;
import com.cowlark.cowbel.ast.nodes.FunctionScopeConstructorNode;
import com.cowlark.cowbel.errors.CompilationException;
import com.cowlark.cowbel.symbols.Symbol;

public class AssignVariablesToConstructorsVisitor extends RecursiveVisitor
{
	@Override
	public void visit(FunctionDefinitionNode node) throws CompilationException
	{
	}
	
	private void visit(AbstractScopeConstructorNode node) throws CompilationException
	{
		Constructor sf = node.getConstructor();
		for (Symbol s : node.getSymbols())
			s.addToConstructor(sf);

		for (AbstractScopeConstructorNode s : node.getImportedScopes())
			sf.addConstructor(s.getConstructor());
		
		super.visit(node);
	}
	
	@Override
	public void visit(BlockScopeConstructorNode node)
	        throws CompilationException
	{
		visit((AbstractScopeConstructorNode) node);
	}
	
	@Override
	public void visit(FunctionScopeConstructorNode node)
	        throws CompilationException
	{
   		visit((AbstractScopeConstructorNode) node);
	}
}
