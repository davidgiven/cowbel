/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel.instructions;

import java.util.List;
import com.cowlark.cowbel.ast.nodes.Node;
import com.cowlark.cowbel.methods.Method;
import com.cowlark.cowbel.symbols.Variable;

public class MethodCallInstruction extends Instruction
{
	private Method _method;
	private Variable _receiver;
	private List<Variable> _invars;
	private List<Variable> _outvars;
	
	public MethodCallInstruction(Node node, Method method, 
			Variable receiver, List<Variable> invars, List<Variable> outvars)
    {
		super(node);
		_method = method;
		_receiver = receiver;
		_invars = invars;
		_outvars = outvars;
    }
	
	public Method getMethod()
	{
		return _method;
	}
	
	public Variable getReceiver()
    {
	    return _receiver;
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
	    return "MethodCall";
	}
	
	@Override
	protected String getShortDescription()
	{
	    return _method.getSignature() + " receiver=" + _receiver.toString() +
	    	" inputs=" + varlist(_invars) + " outputs=" + varlist(_outvars);
	}
	
	@Override
    public void visit(InstructionVisitor visitor)
	{
		visitor.visit(this);
	}
}
