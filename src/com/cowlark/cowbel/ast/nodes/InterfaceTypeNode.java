/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING.BSD in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel.ast.nodes;

import java.util.Collection;
import com.cowlark.cowbel.ast.Visitor;
import com.cowlark.cowbel.errors.CompilationException;
import com.cowlark.cowbel.parser.core.Location;
import com.cowlark.cowbel.types.InterfaceType;
import com.cowlark.cowbel.types.Type;

public class InterfaceTypeNode extends AbstractTypeNode
{
	public InterfaceTypeNode(Location start, Location end)
    {
        super(start, end);
    }
	
	public InterfaceTypeNode(Location start, Location end,
			Collection<FunctionHeaderNode> entries)
    {
        super(start, end);
        addChildren(entries);
    }
		
	@Override
	public Type calculateType()
	{
		return InterfaceType.create(this);
	}
	
	@Override
	public void visit(Visitor visitor) throws CompilationException
	{
		visitor.visit(this);
	}
}