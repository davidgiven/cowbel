/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING.BSD in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel.instructions;

import java.util.List;
import com.cowlark.cowbel.errors.CompilationException;
import com.cowlark.cowbel.interfaces.IsNode;
import com.cowlark.cowbel.symbols.Variable;

public class ExternInstruction extends Instruction
{
	private String _template;
	private List<Variable> _variables;
	
	public ExternInstruction(IsNode node, String template, List<Variable> variables)
    {
		super(node);
		_template = template;
		_variables = variables;
    }

	public String getTemplate()
	{
		return _template;
	}
	
	public List<Variable> getVariables()
    {
	    return _variables;
    }
	
	@Override
	protected String getInstructionName()
	{
	    return "ExternInstruction";
	}
	
	@Override
	protected String getShortDescription()
	{
		return "";
	}
	
	@Override
    public void visit(InstructionVisitor visitor) throws CompilationException
	{
		visitor.visit(this);
	}
}
