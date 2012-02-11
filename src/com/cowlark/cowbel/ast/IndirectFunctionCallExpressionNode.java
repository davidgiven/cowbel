/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING.BSD in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel.ast;

import com.cowlark.cowbel.errors.CompilationException;
import com.cowlark.cowbel.parser.core.Location;

public class IndirectFunctionCallExpressionNode extends AbstractExpressionNode
{
	public IndirectFunctionCallExpressionNode(Location start, Location end)
    {
		super(start, end);
    }
	
	public IndirectFunctionCallExpressionNode(Location start, Location end,
			AbstractExpressionNode object, ExpressionListNode arguments)
    {
		super(start, end);
		addChild(object);
		addChild(arguments);
    }
	
	public AbstractExpressionNode getFunction()
	{
		return (AbstractExpressionNode) getChild(0);
	}

	public ExpressionListNode getArguments()
	{
		return (ExpressionListNode) getChild(1);
	}
	
	@Override
	public void visit(ASTVisitor visitor) throws CompilationException
	{
		visitor.visit(this);
	}
}
