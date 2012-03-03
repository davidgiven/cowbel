/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING.BSD in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel.ast;

import java.util.List;
import com.cowlark.cowbel.errors.CompilationException;
import com.cowlark.cowbel.parser.core.Location;

public class InterfaceListNode extends Node
{
	public InterfaceListNode(Location start, Location end,
			List<TypeVariableNode> ids)
    {
		super(start, end);
		addChildren(ids);
    }
	
	public InterfaceListNode(Location start, Location end,
			TypeVariableNode... ids)
    {
		super(start, end);
		addChildren(ids);
    }
	
	public TypeVariableNode getType(int i)
	{
	    return (TypeVariableNode) getChild(i);
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
