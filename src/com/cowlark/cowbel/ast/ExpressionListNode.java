/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING.BSD in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel.ast;

import java.util.List;
import com.cowlark.cowbel.errors.CompilationException;
import com.cowlark.cowbel.parser.core.Location;

public class ExpressionListNode extends Node
{
	public ExpressionListNode(Location start, Location end,
			List<AbstractExpressionNode> args)
    {
		super(start, end);
		addChildren(args);
    }
	
	public ExpressionListNode(Location start, Location end, AbstractExpressionNode... args)
    {
		super(start, end);
		addChildren(args);
    }
	
	public AbstractExpressionNode getExpression(int i)
	{
	    return (AbstractExpressionNode) getChild(i);
	}
	
	@Override
	public void visit(ASTVisitor visitor) throws CompilationException
	{
		visitor.visit(this);
	}
}
