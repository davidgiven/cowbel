/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel.instructions;

import com.cowlark.cowbel.ast.nodes.Node;

public class StringConstantInstruction extends Instruction
{
	private String _value;
	
	public StringConstantInstruction(Node node, String value)
    {
		super(node, 0);
		_value = value;
    }
	
	public String getValue()
	{
		return _value;
	}
	
	@Override
	protected String getInstructionName()
	{
	    return "StringConstant";
	}
	
	@Override
	protected String getShortDescription()
	{
	    return "<" + _value + ">";
	}
	
	public void visit(InstructionVisitor visitor)
	{
		visitor.visit(this);
	}
}
