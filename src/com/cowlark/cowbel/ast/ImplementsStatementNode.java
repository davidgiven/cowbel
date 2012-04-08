/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING.BSD in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel.ast;

import com.cowlark.cowbel.errors.CompilationException;
import com.cowlark.cowbel.parser.core.Location;

public class ImplementsStatementNode extends AbstractStatementNode
{
	private boolean _isExtern;
	
	public ImplementsStatementNode(Location start, Location end,
			boolean isextern)
    {
		super(start, end);
		_isExtern = isextern;
    }
	
	public ImplementsStatementNode(Location start, Location end,
			AbstractTypeExpressionNode value, boolean isextern)
    {
		super(start, end);
		addChild(value);
		_isExtern = isextern;
    }

	public AbstractTypeExpressionNode getTypeNode()
	{
		return (AbstractTypeExpressionNode) getChild(0);
	}
	
	public boolean isExtern()
    {
	    return _isExtern;
    }
	
	@Override
	public void visit(ASTVisitor visitor) throws CompilationException
	{
		visitor.visit(this);
	}
}
