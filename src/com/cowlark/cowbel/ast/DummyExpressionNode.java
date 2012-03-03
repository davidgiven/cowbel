/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING.BSD in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel.ast;

import com.cowlark.cowbel.errors.CompilationException;
import com.cowlark.cowbel.parser.core.Location;

public class DummyExpressionNode extends AbstractExpressionNode
{
	public DummyExpressionNode(Location start, Location end)
    {
        super(start, end);
    }
	
	public DummyExpressionNode(Location start, Location end, AbstractExpressionNode child)
    {
        super(start, end);
        addChild(child);
    }

	public AbstractExpressionNode getChild()
	{
		return (AbstractExpressionNode) getChild(0);
	}
	
	@Override
	public void visit(ASTVisitor visitor) throws CompilationException
	{
		visitor.visit(this);
	}
}