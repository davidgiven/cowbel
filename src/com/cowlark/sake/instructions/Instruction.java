package com.cowlark.sake.instructions;

import com.cowlark.sake.ast.HasNode;
import com.cowlark.sake.ast.nodes.Node;

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
