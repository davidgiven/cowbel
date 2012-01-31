/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel.instructions;

import java.util.List;
import com.cowlark.cowbel.ast.nodes.Node;
import com.cowlark.cowbel.symbols.Function;
import com.cowlark.cowbel.symbols.Variable;

public class DirectFunctionCallInstruction extends Instruction
{
	private Function _function;
	private List<Variable> _invars;
	private List<Variable> _outvars;
	
	public DirectFunctionCallInstruction(Node node, Function function,
			List<Variable> invars, List<Variable> outvars)
    {
		super(node);
		_function = function;
		_invars = invars;
		_outvars = outvars;
    }
	
	public Function getFunction()
	{
		return _function;
	}
	
	public List<Variable> getInputVariables()
	{
		return _invars;
	}
	
	public List<Variable> getOutputVariables()
    {
	    return _outvars;
    }
	
	@Override
	protected String getInstructionName()
	{
		return "DirectFunctionCall";
	}
	
	@Override
	protected String getShortDescription()
	{
	    return _function.toString() + " inputs=" + varlist(_invars) +
	    	" outputs="	+ varlist(_outvars);
	}
	
	public void visit(InstructionVisitor visitor)
	{
		visitor.visit(this);
	}
}
