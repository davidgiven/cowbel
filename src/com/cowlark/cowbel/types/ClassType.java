/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING.BSD in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel.types;

import com.cowlark.cowbel.ast.HasInputs;
import com.cowlark.cowbel.ast.HasTypeArguments;
import com.cowlark.cowbel.ast.IsMethod;
import com.cowlark.cowbel.ast.nodes.BlockScopeConstructorNode;
import com.cowlark.cowbel.ast.nodes.IdentifierNode;
import com.cowlark.cowbel.ast.nodes.Node;
import com.cowlark.cowbel.errors.CompilationException;
import com.cowlark.cowbel.errors.TypesNotCompatibleException;
import com.cowlark.cowbel.methods.Method;

public class ClassType extends Type
{
	public static ClassType create(BlockScopeConstructorNode block)
	{
		return new ClassType(block);
	}
	
	private BlockScopeConstructorNode _block;
	
	private ClassType(BlockScopeConstructorNode block)
    {
		_block = block;
    }
	
	public BlockScopeConstructorNode getBlock()
    {
	    return _block;
    }
	
	@Override
	public String getCanonicalTypeName()
	{
	    return "{" + getId() + "}";
	}
	
	@Override
	protected void unifyWithImpl(Node node, Type other)
	        throws CompilationException
	{
		throw new TypesNotCompatibleException(node, this, other);
	}
	
	@Override
	public <T extends Node & IsMethod & HasInputs & HasTypeArguments>
		Method lookupMethod(
	        T node, IdentifierNode id) throws CompilationException
	{
		ClassType type = (ClassType) node.getMethodReceiver().getType().getRealType();
		BlockScopeConstructorNode block = type.getBlock();
		return block.lookupMethod(node);
	}
	
	@Override
	public void visit(TypeVisitor visitor)
	{
	    visitor.visit(this);	    
	}
}
