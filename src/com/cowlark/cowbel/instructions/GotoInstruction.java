package com.cowlark.cowbel.instructions;

import com.cowlark.cowbel.BasicBlock;
import com.cowlark.cowbel.ast.nodes.Node;

public class GotoInstruction extends Instruction
{
	private BasicBlock _target;
	
	public GotoInstruction(Node node, BasicBlock target)
	{
		super(node, 0);
		_target = target;
	}
	
	public BasicBlock getTarget()
	{
		return _target;
	}
	
	@Override
	protected String getInstructionName()
	{
		return "Goto";
	}
	
	@Override
	protected String getShortDescription()
	{
	    return _target.toString();
	}	
	
	public void visit(InstructionVisitor visitor)
	{
		visitor.visit(this);
	}
}
