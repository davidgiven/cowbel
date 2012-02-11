/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING.BSD in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel.instructions;

import com.cowlark.cowbel.ast.Node;
import com.cowlark.cowbel.symbols.Variable;

public class RealConstantInstruction extends Instruction
{
	private double _value;
	private Variable _outvar;
	
	public RealConstantInstruction(Node node, double value, Variable outvar)
    {
		super(node);
		_value = value;
		_outvar = outvar;
    }
	
	public double getValue()
	{
		return _value;
	}
	
	public Variable getOutputVariable()
    {
	    return _outvar;
    }
	
	@Override
	protected String getInstructionName()
	{
	    return "RealConstant";
	}
	
	@Override
	protected String getShortDescription()
	{
	    return Double.toString(_value) + " outvar=" + _outvar.toString();
	}
	
	@Override
    public void visit(InstructionVisitor visitor)
	{
		visitor.visit(this);
	}
}
