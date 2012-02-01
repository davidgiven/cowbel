/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel.instructions;

import java.util.List;
import com.cowlark.cowbel.ast.HasNode;
import com.cowlark.cowbel.ast.nodes.Node;
import com.cowlark.cowbel.symbols.Variable;

public abstract class Instruction implements HasNode
{
	private Node _node;
	
	public Instruction(Node node)
    {
		_node = node;
    }
	
	@Override
    public Node getNode()
    {
	    return _node;
    }
	
	public void setNode(Node node)
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
	
	public void visit(InstructionVisitor visitor)
	{
		visitor.visit(this);
	}
}
