package com.cowlark.sake.instructions;

import com.cowlark.sake.ast.nodes.Node;
import com.cowlark.sake.symbols.Function;

public class DirectFunctionCallInstruction extends Instruction
{
	private Function _function;
	
	public DirectFunctionCallInstruction(Node node, Function function, int args)
    {
		super(node, args);
		_function = function;
    }
	
	public Function getFunction()
	{
		return _function;
	}
	
	@Override
	protected String getInstructionName()
	{
		return "DirectFunctionCall";
	}
	
	@Override
	protected String getShortDescription()
	{
	    return _function.toString();
	}
	
	public void visit(InstructionVisitor visitor)
	{
		visitor.visit(this);
	}
}
