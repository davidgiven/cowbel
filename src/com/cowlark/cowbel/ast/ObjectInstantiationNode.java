/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING.BSD in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel.ast;

import java.util.List;
import com.cowlark.cowbel.errors.CompilationException;
import com.cowlark.cowbel.parser.core.Location;

public class ObjectInstantiationNode extends AbstractExpressionNode
{
	public ObjectInstantiationNode(Location start, Location end)
    {
		super(start, end);
    }
	
	public ObjectInstantiationNode(Location start, Location end,
			List<ObjectInstantiationMemberNode> members)
	{
		this(start, end);
		addChildren(members);
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
