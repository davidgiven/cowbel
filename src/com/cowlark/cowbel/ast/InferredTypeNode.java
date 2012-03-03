/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING.BSD in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel.ast;

import com.cowlark.cowbel.core.Interface;
import com.cowlark.cowbel.errors.CompilationException;
import com.cowlark.cowbel.parser.core.Location;

public class InferredTypeNode extends AbstractTypeExpressionNode
{
	public InferredTypeNode(Location start, Location end)
    {
        super(start, end);
    }

	@Override
	public Interface createInterface() throws CompilationException
	{
	    return null;
	}
	
	@Override
	public void visit(ASTVisitor visitor) throws CompilationException
	{
		visitor.visit(this);
	}
}