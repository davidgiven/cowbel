/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING.BSD in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel.ast.nodes;

import com.cowlark.cowbel.ast.Visitor;
import com.cowlark.cowbel.errors.CompilationException;
import com.cowlark.cowbel.parser.core.Location;

public class RealConstantNode extends AbstractExpressionLiteralNode
{
	private double _value;
	
	public RealConstantNode(Location start, Location end, double value)
    {
        super(start, end);
        _value = value;
    }
	
	public double getValue()
	{
		return _value;
	}
	
	@Override
	public String getShortDescription()
	{
		return Double.toString(_value);
	}
	
	@Override
	public void visit(Visitor visitor) throws CompilationException
	{
		visitor.visit(this);
	}
}
