/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel.ast.nodes;

import com.cowlark.cowbel.ast.Visitor;
import com.cowlark.cowbel.errors.CompilationException;
import com.cowlark.cowbel.parser.core.Location;

public class IndirectFunctionCallStatementNode extends StatementNode
{
	public IndirectFunctionCallStatementNode(Location start, Location end,
			ExpressionNode object, IdentifierListNode variables,
			ArgumentListNode arguments)
    {
		super(start, end);
		addChild(object);
		addChild(variables);
		addChild(arguments);
    }
	
	public ExpressionNode getFunction()
	{
		return (ExpressionNode) getChild(0);
	}

	public IdentifierListNode getVariables()
	{
		return (IdentifierListNode) getChild(1);
	}
	
	public ArgumentListNode getArguments()
	{
		return (ArgumentListNode) getChild(2);
	}
	
	@Override
	public void visit(Visitor visitor) throws CompilationException
	{
		visitor.visit(this);
	}
}
