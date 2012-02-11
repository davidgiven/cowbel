/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING.BSD in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel.instructions;

import com.cowlark.cowbel.ast.Node;
import com.cowlark.cowbel.symbols.Variable;

public class VarCopyInstruction extends Instruction
{
	private Variable _invar;
	private Variable _outvar;
	
	public VarCopyInstruction(Node node, Variable invar, Variable outvar)
    {
		super(node);
		_invar = invar;
		_outvar = outvar;
    }
	
	public Variable getInputVariable()
    {
	    return _invar;
    }
	
	public Variable getOutputVariable()
    {
	    return _outvar;
    }
	
	@Override
	protected String getInstructionName()
	{
	    return "VarCopyInstruction";
	}
	
	@Override
	protected String getShortDescription()
	{
	    return _invar.toString() + " -> " + _outvar.toString();
	}
	
	@Override
    public void visit(InstructionVisitor visitor)
	{
		visitor.visit(this);
	}
}
