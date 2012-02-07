/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING.BSD in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel.ast.nodes;

import com.cowlark.cowbel.ast.Visitor;
import com.cowlark.cowbel.errors.CompilationException;
import com.cowlark.cowbel.parser.core.Location;

public class TypeAssignmentNode extends AbstractStatementNode
{
	public TypeAssignmentNode(Location start, Location end)
    {
		super(start, end);
    }
	
	public TypeAssignmentNode(Location start, Location end,
			IdentifierNode identifier, IdentifierListNode typevars,
			AbstractTypeNode type)
    {
		super(start, end);
		addChild(identifier);
		addChild(typevars);
		addChild(type);
    }

	public IdentifierNode getIdentifier()
	{
		return (IdentifierNode) getChild(0);
	}
	
	public IdentifierListNode getTypeVariables()
	{
		return (IdentifierListNode) getChild(1);
	}
	
	public AbstractTypeNode getTypeNode()
	{
		return (AbstractTypeNode) getChild(2);
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
