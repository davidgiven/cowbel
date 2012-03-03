/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING.BSD in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel.ast;

import com.cowlark.cowbel.core.Interface;
import com.cowlark.cowbel.core.InterfaceContext;
import com.cowlark.cowbel.errors.CompilationException;
import com.cowlark.cowbel.interfaces.HasTypeArguments;
import com.cowlark.cowbel.parser.core.Location;

public class TypeVariableNode extends AbstractTypeExpressionNode
		implements HasTypeArguments
{
	public TypeVariableNode(Location start, Location end)
    {
        super(start, end);
    }
	
	public TypeVariableNode(Location start, Location end,
			IdentifierNode node, InterfaceListNode typeassignments)
    {
        super(start, end);
        addChild(node);
        addChild(typeassignments);
    }
	
	public IdentifierNode getIdentifier()
	{
		return (IdentifierNode) getChild(0);
	}
	
	@Override
    public InterfaceListNode getTypeArguments()
	{
		return (InterfaceListNode) getChild(1);
	}
	
	@Override
	public Interface createInterface() throws CompilationException
	{
		InterfaceContext tc = getTypeContext();
		return tc.lookupType(this);
	}
	
	@Override
	public void visit(ASTVisitor visitor) throws CompilationException
	{
		visitor.visit(this);
	}
}