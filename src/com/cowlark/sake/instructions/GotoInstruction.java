package com.cowlark.sake.instructions;

import com.cowlark.sake.BasicBlock;
import com.cowlark.sake.ast.nodes.Node;

public class GotoInstruction extends Instruction
{
	private BasicBlock _target;
	
	public GotoInstruction(Node node, BasicBlock target)
	{
		super(node, 0);
		_target = target;
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
}
