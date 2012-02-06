/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING.BSD in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel;

import java.util.Map;
import java.util.Set;
import com.cowlark.cowbel.ast.RecursiveVisitor;
import com.cowlark.cowbel.ast.nodes.AbstractScopeConstructorNode;
import com.cowlark.cowbel.ast.nodes.BlockExpressionNode;
import com.cowlark.cowbel.ast.nodes.BlockScopeConstructorNode;
import com.cowlark.cowbel.ast.nodes.DoWhileStatementNode;
import com.cowlark.cowbel.ast.nodes.FunctionDefinitionNode;
import com.cowlark.cowbel.ast.nodes.FunctionScopeConstructorNode;
import com.cowlark.cowbel.ast.nodes.WhileStatementNode;
import com.cowlark.cowbel.errors.CompilationException;
import com.cowlark.cowbel.methods.VirtualMethod;
import com.cowlark.cowbel.types.InterfaceType;

public class AssignConstructorsToScopesVisitor extends RecursiveVisitor
{
	private Set<Constructor> _constructors;
	
	public AssignConstructorsToScopesVisitor(Set<Constructor> constructors)
    {
		_constructors = constructors;
    }
	
	private void create(AbstractScopeConstructorNode node)
	{
		if (node.getConstructor() != null)
			return;
		
		Constructor constructor = new Constructor(node);
		_constructors.add(constructor);
		node.setConstructor(constructor);
	}
	
	private void persist(AbstractScopeConstructorNode node)
	{
		inherit(node);
		Constructor constructor = node.getConstructor();
		
		constructor.setPersistent(true);
		
		for (InterfaceType i : node.getInterfaces())
			constructor.addInterface(i);
	}
	
	private void inherit(AbstractScopeConstructorNode node)
	{
		if (node.getConstructor() != null)
			return;
		
		Constructor sf = node.getScope().getConstructor();
		node.setConstructor(sf);
	}
	
	@Override
	public void visit(WhileStatementNode node) throws CompilationException
	{
		AbstractScopeConstructorNode body = node.getBodyStatement();
		create(body);
		
	    super.visit(node);
	}
	
	@Override
	public void visit(DoWhileStatementNode node) throws CompilationException
	{
		AbstractScopeConstructorNode body = node.getBodyStatement();
		create(body);
		
	    super.visit(node);
	}
	
	private void register_functions(AbstractScopeConstructorNode node)
	{
		Constructor constructor = node.getConstructor();
		for (Map.Entry<VirtualMethod, Function> e : node.getVirtualMethods())
			constructor.addVirtualMethod(e.getKey(), e.getValue());
	}
	
	@Override
	public void visit(BlockScopeConstructorNode node) throws CompilationException
	{
		inherit(node);
		register_functions(node);
		super.visit(node);
	}
	
	@Override
	public void visit(FunctionScopeConstructorNode node)
	        throws CompilationException
	{
		create(node);
		register_functions(node);
	    super.visit(node);
	}

	@Override
	public void visit(FunctionDefinitionNode node) throws CompilationException
	{
		AbstractScopeConstructorNode scope = node.getScope();
		persist(scope);
		/* don't recurse */
	}
	
	@Override
	public void visit(BlockExpressionNode node) throws CompilationException
	{
		AbstractScopeConstructorNode scope = node.getBlock();
		persist(scope);
		
	    super.visit(node);
	}
	
}
