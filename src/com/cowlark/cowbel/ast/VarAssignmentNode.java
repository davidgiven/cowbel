/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING.BSD in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel.ast;

import com.cowlark.cowbel.errors.CompilationException;
import com.cowlark.cowbel.parser.core.Location;

public class VarAssignmentNode extends AbstractStatementNode
{
	public VarAssignmentNode(Location start, Location end)
    {
		super(start, end);
    }
	
	public VarAssignmentNode(Location start, Location end,
			IdentifierListNode identifier, ExpressionListNode value)
    {
		super(start, end);
		addChild(identifier);
		addChild(value);
    }
	
	public IdentifierListNode getVariables()
	{
		return (IdentifierListNode) getChild(0);
	}
	
	public ExpressionListNode getExpressions()
	{
		return (ExpressionListNode) getChild(1);
	}
	
	@Override
	public void visit(ASTVisitor visitor) throws CompilationException
	{
		visitor.visit(this);
	}
}
