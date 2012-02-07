/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING.BSD in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel;

import java.util.Map;
import com.cowlark.cowbel.ast.RecursiveVisitor;
import com.cowlark.cowbel.ast.nodes.AbstractScopeConstructorNode;
import com.cowlark.cowbel.ast.nodes.BlockScopeConstructorNode;
import com.cowlark.cowbel.ast.nodes.FunctionScopeConstructorNode;
import com.cowlark.cowbel.errors.CompilationException;
import com.cowlark.cowbel.methods.VirtualMethod;

public class AssignConstructorsToScopesVisitor extends RecursiveVisitor
{
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
		
		for (Map.Entry<VirtualMethod, Function> e : node.getVirtualMethods())
			constructor.addVirtualMethod(e.getKey(), e.getValue());
		node.setConstructor(constructor);
	}
	
	@Override
	public void visit(BlockScopeConstructorNode node) throws CompilationException
	{
		visit((AbstractScopeConstructorNode) node);
		super.visit(node);
	}
	
	@Override
	public void visit(FunctionScopeConstructorNode node)
	        throws CompilationException
	{
		visit((AbstractScopeConstructorNode) node);
	    super.visit(node);
	}
}
