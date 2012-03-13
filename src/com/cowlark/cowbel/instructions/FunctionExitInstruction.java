/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING.BSD in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel.instructions;

import com.cowlark.cowbel.errors.CompilationException;
import com.cowlark.cowbel.interfaces.IsNode;

public class FunctionExitInstruction extends Instruction
{
	public FunctionExitInstruction(IsNode node)
    {
		super(node);
    }
	
	@Override
	protected String getInstructionName()
	{
		return "FunctionExit";
	}
	
	@Override
    public void visit(InstructionVisitor visitor) throws CompilationException
	{
		visitor.visit(this);
	}
}
