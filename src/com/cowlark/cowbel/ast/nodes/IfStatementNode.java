/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel.ast.nodes;

import com.cowlark.cowbel.ast.Visitor;
import com.cowlark.cowbel.errors.CompilationException;
import com.cowlark.cowbel.parser.core.Location;

public class IfStatementNode extends StatementNode
{
	public IfStatementNode(Location start, Location end,
			ExpressionNode conditional,
			ScopeConstructorNode positive)
    {
		super(start, end);
		addChild(conditional);
		addChild(positive);
    }
	
	public ExpressionNode getConditionalExpression()
	{
		return (ExpressionNode) getChild(0);
	}
	
	public ScopeConstructorNode getPositiveStatement()
	{
		return (ScopeConstructorNode) getChild(1);
	}
	
	@Override
	public String getShortDescription()
	{
	    return getText();
	}
	
	@Override
	public void visit(Visitor visitor) throws CompilationException
	{
		visitor.visit(this);
	}
}
