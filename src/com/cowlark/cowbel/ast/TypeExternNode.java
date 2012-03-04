/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING.BSD in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel.ast;

import com.cowlark.cowbel.errors.CompilationException;
import com.cowlark.cowbel.parser.core.Location;

public class TypeExternNode extends AbstractStatementNode
{
	public TypeExternNode(Location start, Location end)
    {
		super(start, end);
    }
	
	public TypeExternNode(Location start, Location end,
			StringConstantNode stringpr)
    {
		super(start, end);
		addChild(stringpr);
    }

	public StringConstantNode getExternType()
	{
		return (StringConstantNode) getChild(0);
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
