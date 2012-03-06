/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING.BSD in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel.core;

import com.cowlark.cowbel.ast.AbstractScopeConstructorNode;
import com.cowlark.cowbel.ast.BlockExpressionNode;
import com.cowlark.cowbel.ast.BlockScopeConstructorNode;
import com.cowlark.cowbel.ast.FunctionDefinitionNode;
import com.cowlark.cowbel.ast.FunctionScopeConstructorNode;
import com.cowlark.cowbel.ast.RecursiveASTVisitor;
import com.cowlark.cowbel.errors.CompilationException;
import com.cowlark.cowbel.types.ObjectConcreteType;

public class AssignConstructorsToScopesVisitor extends RecursiveASTVisitor
{
	public static AssignConstructorsToScopesVisitor Instance =
		new AssignConstructorsToScopesVisitor();
	
	private void visit(AbstractScopeConstructorNode node)
		throws CompilationException
	{
		Constructor constructor;
		
		switch (node.getScopeType())
		{
			case TRIVIAL:
			{
				constructor = node.getScope().getConstructor();
				break;
			}
			
			case SIGNIFICANT:
			{
				constructor = new Constructor(node);
				break;
			}
			
			case PERSISTENT:
			{
				constructor = new Constructor(node);
				constructor.setPersistent(true);
				break;
			}
			
			default:
				assert(false);
				throw null;
		}
		
		node.setConstructor(constructor);
	}
	
	@Override
	public void visit(FunctionDefinitionNode node) throws CompilationException
	{
		/* Do not recurse into nested functions */
	}
	
	@Override
	public void visit(BlockScopeConstructorNode node) throws CompilationException
	{
		visit((AbstractScopeConstructorNode) node);
		super.visit(node);
	}
		
	@Override
	public void visit(BlockExpressionNode node) throws CompilationException
	{
	    super.visit(node);
	    Constructor constructor = node.getBlock().getConstructor();
	    ObjectConcreteType ctype = (ObjectConcreteType) node.getTypeRef().getConcreteType();
	    constructor.addObject(ctype);
	}
	
	@Override
	public void visit(FunctionScopeConstructorNode node)
	        throws CompilationException
	{
		visit((AbstractScopeConstructorNode) node);
	    super.visit(node);
	}
}
