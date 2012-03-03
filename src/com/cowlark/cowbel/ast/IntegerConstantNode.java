/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING.BSD in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel.ast;

import com.cowlark.cowbel.errors.CompilationException;
import com.cowlark.cowbel.parser.core.Location;

public class IntegerConstantNode extends AbstractExpressionLiteralNode
{
	private long _value;
	
	public IntegerConstantNode(Location start, Location end, long value)
    {
        super(start, end, "int");
        _value = value;
    }
	
	public long getValue()
	{
		return _value;
	}
	
	@Override
	public String getShortDescription()
	{
		return Long.toString(_value);
	}
	
	@Override
	public void visit(ASTVisitor visitor) throws CompilationException
	{
		visitor.visit(this);
	}
}
