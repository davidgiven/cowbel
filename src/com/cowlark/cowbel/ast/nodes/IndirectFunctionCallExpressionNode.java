/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel.ast.nodes;

import com.cowlark.cowbel.ast.Visitor;
import com.cowlark.cowbel.errors.CompilationException;
import com.cowlark.cowbel.parser.core.Location;

public class IndirectFunctionCallExpressionNode extends ExpressionNode
{
	public IndirectFunctionCallExpressionNode(Location start, Location end,
			ExpressionNode object, ExpressionListNode arguments)
    {
		super(start, end);
		addChild(object);
		addChild(arguments);
    }
	
	public ExpressionNode getFunction()
	{
		return (ExpressionNode) getChild(0);
	}

	public ExpressionListNode getArguments()
	{
		return (ExpressionListNode) getChild(1);
	}
	
	@Override
	public void visit(Visitor visitor) throws CompilationException
	{
		visitor.visit(this);
	}
}
