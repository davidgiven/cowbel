/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING.BSD in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel.ast;

import com.cowlark.cowbel.errors.CompilationException;
import com.cowlark.cowbel.interfaces.HasIdentifier;
import com.cowlark.cowbel.parser.core.Location;

public class TypeAssignmentNode extends AbstractStatementNode
		implements HasIdentifier
{
	public TypeAssignmentNode(Location start, Location end)
    {
		super(start, end);
    }
	
	public TypeAssignmentNode(Location start, Location end,
			IdentifierNode identifier, IdentifierListNode typevars,
			AbstractTypeExpressionNode type)
    {
		super(start, end);
		addChild(identifier);
		addChild(typevars);
		addChild(type);
    }

	@Override
    public IdentifierNode getIdentifier()
	{
		return (IdentifierNode) getChild(0);
	}
	
	public IdentifierListNode getTypeVariables()
	{
		return (IdentifierListNode) getChild(1);
	}
	
	public AbstractTypeExpressionNode getTypeNode()
	{
		return (AbstractTypeExpressionNode) getChild(2);
	}
	
	@Override
	public String getShortDescription()
	{
	    return getText();
	}
	
	@Override
	public void visit(ASTVisitor visitor) throws CompilationException
	{
		visitor.visit(this);
	}
}
