/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel.ast.nodes;

import com.cowlark.cowbel.ast.Visitor;
import com.cowlark.cowbel.errors.CompilationException;
import com.cowlark.cowbel.parser.core.Location;
import com.cowlark.cowbel.types.IntegerType;
import com.cowlark.cowbel.types.Type;

public class IntegerTypeNode extends TypeNode
{
	public IntegerTypeNode(Location start, Location end)
    {
        super(start, end);
    }
	
	@Override
    protected Type getTypeImpl()
	{
	    return IntegerType.create();
	}
	
	@Override
	public void visit(Visitor visitor) throws CompilationException
	{
		visitor.visit(this);
	}
}