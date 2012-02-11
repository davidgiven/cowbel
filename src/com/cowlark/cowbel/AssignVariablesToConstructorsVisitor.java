/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING.BSD in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel;

import com.cowlark.cowbel.ast.AbstractScopeConstructorNode;
import com.cowlark.cowbel.ast.BlockScopeConstructorNode;
import com.cowlark.cowbel.ast.FunctionDefinitionNode;
import com.cowlark.cowbel.ast.FunctionScopeConstructorNode;
import com.cowlark.cowbel.ast.RecursiveASTVisitor;
import com.cowlark.cowbel.errors.CompilationException;
import com.cowlark.cowbel.symbols.Symbol;
import com.cowlark.cowbel.types.InterfaceType;

public class AssignVariablesToConstructorsVisitor extends RecursiveASTVisitor
{
	@Override
	public void visit(FunctionDefinitionNode node) throws CompilationException
	{
	}
	
	private void visit(AbstractScopeConstructorNode node) throws CompilationException
	{
		Constructor c = node.getConstructor();
		
		for (InterfaceType i : node.getInterfaces())
			c.addInterface(i);
		
		for (Symbol s : node.getSymbols())
			s.addToConstructor(c);

		for (AbstractScopeConstructorNode s : node.getImportedScopes())
			c.addConstructor(s.getConstructor());
		
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
