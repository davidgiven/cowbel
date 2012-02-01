/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel.instructions;

import com.cowlark.cowbel.BasicBlock;
import com.cowlark.cowbel.ast.nodes.Node;
import com.cowlark.cowbel.symbols.Variable;

public class IfInstruction extends Instruction
{
	private Variable _condition;
	private BasicBlock _positive;
	private BasicBlock _negative;
	
	public IfInstruction(Node node, Variable condition,
			BasicBlock positive, BasicBlock negative)
    {
		super(node);
		_condition = condition;
		_positive = positive;
		_negative = negative;
    }
	
	public Variable getCondition()
    {
	    return _condition;
    }
	
	public BasicBlock getPositiveTarget()
	{
		return _positive;
	}
	
	public BasicBlock getNegativeTarget()
	{
		return _negative;
	}

	@Override
	protected String getInstructionName()
	{
	    return "If";
	}
	
	@Override
	protected String getShortDescription()
	{
	    return "condition=" + _condition.toString() + " +"+_positive + " -"+_negative;
	}
	
	@Override
    public void visit(InstructionVisitor visitor)
	{
		visitor.visit(this);
	}
}
