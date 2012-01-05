package com.cowlark.sake.instructions;

import com.cowlark.sake.ast.nodes.Node;

public class FunctionCallInstruction extends Instruction
{
	private int _args;
	
	public FunctionCallInstruction(Node node, int args)
    {
		super(node, args+1);
		_args = args;
    }
	
	@Override
	protected String getInstructionName()
	{
		return "FunctionCall";
	}
	
	public void visit(InstructionVisitor visitor)
	{
		visitor.visit(this);
	}
}
