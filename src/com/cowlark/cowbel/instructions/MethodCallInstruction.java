/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING.BSD in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel.instructions;

import java.util.List;
import com.cowlark.cowbel.core.Callable;
import com.cowlark.cowbel.errors.CompilationException;
import com.cowlark.cowbel.interfaces.IsNode;
import com.cowlark.cowbel.symbols.Variable;

public class MethodCallInstruction extends Instruction
		implements HasInputVariables, HasOutputVariables
{
	private Callable _callable;
	private Variable _receiver;
	private List<Variable> _invars;
	private List<Variable> _outvars;
	
	public MethodCallInstruction(IsNode node, Callable callable, 
			Variable receiver, List<Variable> invars, List<Variable> outvars)
    {
		super(node);
		_callable = callable;
		_receiver = receiver;
		_invars = invars;
		_outvars = outvars;
    }
	
	public Callable getCallable()
	{
		return _callable;
	}
	
	public Variable getReceiver()
    {
	    return _receiver;
    }
	
	@Override
	public List<Variable> getInputVariables()
    {
	    return _invars;
    }
	
	@Override
	public List<Variable> getOutputVariables()
    {
	    return _outvars;
    }
	
	@Override
	protected String getInstructionName()
	{
	    return "MethodCall";
	}
	
	@Override
	protected String getShortDescription()
	{
	    return _callable.getName().getText() +
	    	" receiver=" + _receiver.toString() +
	    	" inputs=" + varlist(_invars) +
	    	" outputs=" + varlist(_outvars);
	}
	
	@Override
    public void visit(InstructionVisitor visitor) throws CompilationException
	{
		visitor.visit(this);
	}
}
