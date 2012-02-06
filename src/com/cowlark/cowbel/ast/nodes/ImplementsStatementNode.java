/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING.BSD in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel.ast.nodes;

import com.cowlark.cowbel.ast.Visitor;
import com.cowlark.cowbel.errors.CompilationException;
import com.cowlark.cowbel.parser.core.Location;

public class ImplementsStatementNode extends AbstractStatementNode
{
	public ImplementsStatementNode(Location start, Location end)
    {
		super(start, end);
    }
	
	public ImplementsStatementNode(Location start, Location end,
			AbstractTypeNode value)
    {
		super(start, end);
		addChild(value);
    }

	public AbstractTypeNode getTypeNode()
	{
		return (AbstractTypeNode) getChild(0);
	}
	
	@Override
	public void visit(Visitor visitor) throws CompilationException
	{
		visitor.visit(this);
	}
}
