/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING.BSD in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel.instructions;

import com.cowlark.cowbel.ast.Node;

public class FunctionExitInstruction extends Instruction
{
	public FunctionExitInstruction(Node node)
    {
		super(node);
    }
	
	@Override
	protected String getInstructionName()
	{
		return "FunctionExit";
	}
	
	@Override
    public void visit(InstructionVisitor visitor)
	{
		visitor.visit(this);
	}
}
