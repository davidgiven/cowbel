/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel.ast.nodes;

import com.cowlark.cowbel.ast.Visitor;
import com.cowlark.cowbel.errors.CompilationException;
import com.cowlark.cowbel.parser.core.Location;

public class VarAssignmentNode extends StatementNode
{
	public VarAssignmentNode(Location start, Location end,
			IdentifierListNode identifier, ExpressionNode value)
    {
		super(start, end);
		addChild(identifier);
		addChild(value);
    }
	
	public IdentifierListNode getVariables()
	{
		return (IdentifierListNode) getChild(0);
	}
	
	public ExpressionNode getExpression()
	{
		return (ExpressionNode) getChild(1);
	}
	
	@Override
	public void visit(Visitor visitor) throws CompilationException
	{
		visitor.visit(this);
	}
}
