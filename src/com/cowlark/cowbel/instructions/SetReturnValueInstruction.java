/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel.instructions;

import com.cowlark.cowbel.ast.nodes.Node;

public class SetReturnValueInstruction extends Instruction
{
	public SetReturnValueInstruction(Node node)
    {
		super(node, 1);
    }
	
	@Override
	protected String getInstructionName()
	{
		return "SetReturnValue";
	}
	
	public void visit(InstructionVisitor visitor)
	{
		visitor.visit(this);
	}
}
