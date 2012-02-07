/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING.BSD in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel.ast.nodes;

import com.cowlark.cowbel.ast.Visitor;
import com.cowlark.cowbel.errors.CompilationException;
import com.cowlark.cowbel.parser.core.Location;
import com.cowlark.cowbel.types.Type;

public class TypeVariableNode extends AbstractTypeNode
{
	public TypeVariableNode(Location start, Location end)
    {
        super(start, end);
    }
	
	public TypeVariableNode(Location start, Location end,
			IdentifierNode node, TypeListNode typeassignments)
    {
        super(start, end);
        addChild(node);
        addChild(typeassignments);
    }
	
	public IdentifierNode getIdentifier()
	{
		return (IdentifierNode) getChild(0);
	}
	
	public TypeListNode getTypeAssignments()
	{
		return (TypeListNode) getChild(1);
	}
	
	@Override
	public Type calculateType() throws CompilationException
	{
		return getTypeContext().lookupType(this);
	}
	
	@Override
	public void visit(Visitor visitor) throws CompilationException
	{
		visitor.visit(this);
	}
}