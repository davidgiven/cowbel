/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING.BSD in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel.instructions;

import java.util.List;
import com.cowlark.cowbel.errors.CompilationException;
import com.cowlark.cowbel.interfaces.HasNode;
import com.cowlark.cowbel.interfaces.IsNode;
import com.cowlark.cowbel.symbols.Variable;

public abstract class Instruction implements HasNode
{
	private IsNode _node;
	
	public Instruction(IsNode node)
    {
		_node = node;
    }
	
	@Override
    public IsNode getNode()
    {
	    return _node;
    }
	
	public void setNode(IsNode node)
    {
	    _node = node;
    }
	
	protected abstract String getInstructionName();
	
	protected String getShortDescription()
	{
		return "";
	}
	
	@Override
	public String toString()
	{
	    return getInstructionName() + " " +
	    	getShortDescription();
	}
	
	protected String varlist(List<Variable> vars)
	{
		StringBuilder sb = new StringBuilder();
		sb.append('(');
		
		boolean first = true;
		for (Variable v : vars)
		{
			if (!first)
				sb.append(", ");
			first = false;
			
			sb.append(v.toString());
		}
		
		sb.append(')');
		return sb.toString();
	}
	
	public void visit(InstructionVisitor visitor) throws CompilationException
	{
		visitor.visit(this);
	}
}
