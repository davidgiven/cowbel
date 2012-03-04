/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING.BSD in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel.instructions;

import com.cowlark.cowbel.ast.Node;
import com.cowlark.cowbel.core.BasicBlock;

public class GotoInstruction extends Instruction
{
	private BasicBlock _target;
	
	public GotoInstruction(Node node, BasicBlock target)
	{
		super(node);
		_target = target;
	}
	
	public BasicBlock getTarget()
	{
		return _target;
	}
	
	@Override
	protected String getInstructionName()
	{
		return "Goto";
	}
	
	@Override
	protected String getShortDescription()
	{
	    return _target.toString();
	}	
	
	@Override
    public void visit(InstructionVisitor visitor)
	{
		visitor.visit(this);
	}
}
