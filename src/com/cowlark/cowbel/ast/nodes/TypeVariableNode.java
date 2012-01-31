/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel.ast.nodes;

import com.cowlark.cowbel.ast.Visitor;
import com.cowlark.cowbel.errors.CompilationException;
import com.cowlark.cowbel.parser.core.Location;
import com.cowlark.cowbel.types.Type;
import com.cowlark.cowbel.types.TypeVariable;

public class TypeVariableNode extends TypeNode
{
	public TypeVariableNode(Location start, Location end,
			IdentifierNode node)
    {
        super(start, end);
        addChild(node);
    }
	
	public IdentifierNode getIdentifier()
	{
		return (IdentifierNode) getChild(0);
	}
	
	@Override
    protected Type getTypeImpl()
	{
	    return TypeVariable.create(getIdentifier());
	}
	
	@Override
	public void visit(Visitor visitor) throws CompilationException
	{
		visitor.visit(this);
	}
}