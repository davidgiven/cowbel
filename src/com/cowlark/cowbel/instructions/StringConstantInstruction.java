/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel.instructions;

import com.cowlark.cowbel.ast.nodes.Node;
import com.cowlark.cowbel.symbols.Variable;

public class StringConstantInstruction extends Instruction
{
	private String _value;
	private Variable _outvar;
	
	public StringConstantInstruction(Node node, String value, Variable outvar)
    {
		super(node);
		_value = value;
		_outvar = outvar;
    }
	
	public String getValue()
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
	    return "StringConstant";
	}
	
	@Override
	protected String getShortDescription()
	{
	    return "<" + _value + "> output=" + _outvar.toString();
	}
	
	@Override
    public void visit(InstructionVisitor visitor)
	{
		visitor.visit(this);
	}
}
