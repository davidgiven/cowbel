/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel.ast.nodes;

import com.cowlark.cowbel.ast.Visitor;
import com.cowlark.cowbel.errors.CompilationException;
import com.cowlark.cowbel.parser.core.Location;

public class WhileStatementNode extends AbstractStatementNode
{
	public WhileStatementNode(Location start, Location end)
    {
		super(start, end);
    }
	
	public WhileStatementNode(Location start, Location end,
			AbstractExpressionNode conditional,
			AbstractScopeConstructorNode body)
    {
		super(start, end);
		addChild(conditional);
		addChild(body);
    }
	
	public AbstractExpressionNode getConditionalExpression()
	{
		return (AbstractExpressionNode) getChild(0);
	}
	
	public AbstractScopeConstructorNode getBodyStatement()
	{
		return (AbstractScopeConstructorNode) getChild(1);
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
