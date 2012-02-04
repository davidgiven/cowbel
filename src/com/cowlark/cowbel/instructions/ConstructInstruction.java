/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING.BSD in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel.instructions;

import com.cowlark.cowbel.Constructor;
import com.cowlark.cowbel.ast.nodes.Node;

public class ConstructInstruction extends Instruction
{
	private Constructor _constructor;
	
	public ConstructInstruction(Node node, Constructor constructor)
    {
		super(node);
		_constructor = constructor;
    }
	
	public Constructor getConstructor()
	{
		return _constructor;
	}
	
	@Override
	protected String getInstructionName()
	{
	    return "Construct";
	}
	
	@Override
	protected String getShortDescription()
	{
	    return _constructor.toString();
	}
	
	@Override
    public void visit(InstructionVisitor visitor)
	{
		visitor.visit(this);
	}
}
