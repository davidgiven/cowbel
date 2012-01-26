/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel.ast.nodes;

import com.cowlark.cowbel.ast.Visitor;
import com.cowlark.cowbel.errors.CompilationException;
import com.cowlark.cowbel.parser.core.Location;
import com.cowlark.cowbel.types.ArrayType;
import com.cowlark.cowbel.types.Type;

public class ArrayTypeNode extends TypeNode
{
	public ArrayTypeNode(Location start, Location end, TypeNode childpr)
    {
        super(start, end);
        addChild(childpr);
    }
	
	public TypeNode getChildTypeNode()
	{
		return (TypeNode) getChild(0);
	}
	
	@Override
    protected Type getTypeImpl()
	{
	    return ArrayType.create(getChildTypeNode().getType());
	}
	
	@Override
	public void visit(Visitor visitor) throws CompilationException
	{
		visitor.visit(this);
	}
}