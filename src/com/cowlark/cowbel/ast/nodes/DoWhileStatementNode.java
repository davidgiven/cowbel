/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel.ast.nodes;

import com.cowlark.cowbel.ast.Visitor;
import com.cowlark.cowbel.errors.CompilationException;
import com.cowlark.cowbel.parser.core.Location;

public class DoWhileStatementNode extends StatementNode
{
	public DoWhileStatementNode(Location start, Location end,
			ScopeConstructorNode body,
			ExpressionNode conditional)
    {
		super(start, end);
		addChild(body);
		addChild(conditional);
    }
	
	public ScopeConstructorNode getBodyStatement()
	{
		return (ScopeConstructorNode) getChild(0);
	}
	
	public ExpressionNode getConditionalExpression()
	{
		return (ExpressionNode) getChild(1);
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
	
	@Override
	public boolean isLoopingNode()
	{
	    return true;
	}
}
