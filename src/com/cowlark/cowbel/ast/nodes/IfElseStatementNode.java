/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING.BSD in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel.ast.nodes;

import com.cowlark.cowbel.ast.Visitor;
import com.cowlark.cowbel.errors.CompilationException;
import com.cowlark.cowbel.parser.core.Location;

public class IfElseStatementNode extends AbstractStatementNode
{
	public IfElseStatementNode(Location start, Location end)
    {
		super(start, end);
    }
	
	public IfElseStatementNode(Location start, Location end,
			AbstractExpressionNode conditional,
			AbstractScopeConstructorNode positive,
			AbstractScopeConstructorNode negative)
    {
		super(start, end);
		addChild(conditional);
		addChild(positive);
		addChild(negative);
    }
	
	public AbstractExpressionNode getConditionalExpression()
	{
		return (AbstractExpressionNode) getChild(0);
	}
	
	public AbstractScopeConstructorNode getPositiveStatement()
	{
		return (AbstractScopeConstructorNode) getChild(1);
	}
	
	public AbstractScopeConstructorNode getNegativeStatement()
	{
		return (AbstractScopeConstructorNode) getChild(2);
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
