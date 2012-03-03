/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING.BSD in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel.ast;

import com.cowlark.cowbel.errors.CompilationException;
import com.cowlark.cowbel.interfaces.HasConcreteType;
import com.cowlark.cowbel.parser.core.Location;
import com.cowlark.cowbel.types.AbstractConcreteType;
import com.cowlark.cowbel.types.ObjectConcreteType;

public class BlockExpressionNode extends AbstractExpressionNode
		implements HasConcreteType
{
	public BlockExpressionNode(Location start, Location end)
	{
		super(start, end);
	}
	
	public BlockExpressionNode(Location start, Location end,
			BlockScopeConstructorNode block)
    {
        super(start, end);
        addChild(block);
    }
	
	public BlockScopeConstructorNode getBlock()
	{
		return (BlockScopeConstructorNode) getChild(0);
	}
	
	@Override
	public void visit(ASTVisitor visitor) throws CompilationException
	{
		visitor.visit(this);
	}
	
	@Override
	public AbstractConcreteType createConcreteType()
	{
		return new ObjectConcreteType(this);
	}
}
