/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel.instructions;

import com.cowlark.cowbel.ast.nodes.Node;
import com.cowlark.cowbel.symbols.Variable;

public class IntegerConstantInstruction extends Instruction
{
	private long _value;
	private Variable _outvar;
	
	public IntegerConstantInstruction(Node node, long value, Variable outvar)
    {
		super(node);
		_value = value;
		_outvar = outvar;
    }
	
	public long getValue()
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
	    return "IntegerConstant";
	}
	
	@Override
	protected String getShortDescription()
	{
	    return Long.toString(_value) + " outvar=" + _outvar.toString();
	}
	
	public void visit(InstructionVisitor visitor)
	{
		visitor.visit(this);
	}
}
