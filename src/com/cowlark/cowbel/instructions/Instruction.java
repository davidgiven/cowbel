/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel.instructions;

import com.cowlark.cowbel.ast.HasNode;
import com.cowlark.cowbel.ast.nodes.Node;

public abstract class Instruction implements HasNode
{
	private Node _node;
	private int _operands;
	
	public Instruction(Node node, int operands)
    {
		_operands = operands;
		setNode(node);
    }
	
	public Node getNode()
    {
	    return _node;
    }
	
	public void setNode(Node node)
    {
	    _node = node;
    }
	
	public int getNumberOfOperands()
	{
		return _operands;
	}
	
	protected abstract String getInstructionName();
	
	protected String getShortDescription()
	{
		return "";
	}
	
	@Override
	public String toString()
	{
	    return getInstructionName() + " (" + _operands + ") " +
	    	getShortDescription();
	}
	
	public void visit(InstructionVisitor visitor)
	{
		visitor.visit(this);
	}
}
