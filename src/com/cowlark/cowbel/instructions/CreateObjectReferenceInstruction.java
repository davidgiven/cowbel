/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING.BSD in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel.instructions;

import com.cowlark.cowbel.ast.Node;
import com.cowlark.cowbel.core.Constructor;
import com.cowlark.cowbel.symbols.Variable;

public class CreateObjectReferenceInstruction extends Instruction
{
	private Constructor _constructor;
	private Variable _outvar;
	
	public CreateObjectReferenceInstruction(Node node,
			Constructor constructor, Variable outvar)
    {
		super(node);
		_constructor = constructor;
		_outvar = outvar;
    }

	public Constructor getConstructor()
	{
		return _constructor;
	}
	
	public Variable getOutputVariable()
    {
	    return _outvar;
    }

	@Override
	protected String getInstructionName()
	{
	    return "CreateObjectReference";
	}
	
	@Override
	protected String getShortDescription()
	{
	    return _constructor.toString() + " -> " + _outvar.toString();
	}
	
	@Override
    public void visit(InstructionVisitor visitor)
	{
		visitor.visit(this);
	}
}
