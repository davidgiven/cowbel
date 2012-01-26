/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel.instructions;

import com.cowlark.cowbel.ast.nodes.Node;
import com.cowlark.cowbel.symbols.Function;

public class DirectFunctionCallInstruction extends Instruction
{
	private Function _function;
	
	public DirectFunctionCallInstruction(Node node, Function function, int args)
    {
		super(node, args);
		_function = function;
    }
	
	public Function getFunction()
	{
		return _function;
	}
	
	@Override
	protected String getInstructionName()
	{
		return "DirectFunctionCall";
	}
	
	@Override
	protected String getShortDescription()
	{
	    return _function.toString();
	}
	
	public void visit(InstructionVisitor visitor)
	{
		visitor.visit(this);
	}
}
