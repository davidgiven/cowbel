/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING.BSD in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel.ast.nodes;

import com.cowlark.cowbel.ast.Visitor;
import com.cowlark.cowbel.errors.CompilationException;
import com.cowlark.cowbel.parser.core.Location;

public class ReturnStatementNode extends AbstractStatementNode
{
	public ReturnStatementNode(Location start, Location end)
    {
		super(start, end);
    }
	
	public ReturnStatementNode(Location start, Location end,
			AbstractExpressionNode value)
    {
		super(start, end);
		addChild(value);
    }
	
	public AbstractExpressionNode getValue()
	{
		return (AbstractExpressionNode) getChild(0);
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
