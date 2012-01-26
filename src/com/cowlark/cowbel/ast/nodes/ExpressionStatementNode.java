/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel.ast.nodes;

import com.cowlark.cowbel.ast.Visitor;
import com.cowlark.cowbel.errors.CompilationException;
import com.cowlark.cowbel.parser.core.Location;
import com.cowlark.cowbel.parser.core.ParseResult;

public class ExpressionStatementNode extends StatementNode
{
	public ExpressionStatementNode(Location start, Location end, ParseResult expr)
    {
        super(start, end);
        addChild((ExpressionNode) expr);
    }
	
	public ExpressionNode getExpression()
    {
	    return (ExpressionNode) getChild(0);
    }
	
	@Override
	public void visit(Visitor visitor) throws CompilationException
	{
		visitor.visit(this);
	}
}