/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel.instructions;

import com.cowlark.cowbel.ast.nodes.Node;
import com.cowlark.cowbel.symbols.Variable;

public class GetLocalInstruction extends Instruction
{
	private Variable _var;
	
	public GetLocalInstruction(Node node, Variable var)
	{
		super(node, 0);
		_var = var;
	}	
	
	public Variable getVariable()
	{
		return _var;
	}
	
	@Override
	protected String getInstructionName()
	{
	    return "GetLocal";
	}
	
	@Override
	protected String getShortDescription()
	{
		return _var.getSymbolName().getText();
	}
	
	public void visit(InstructionVisitor visitor)
	{
		visitor.visit(this);
	}
}
