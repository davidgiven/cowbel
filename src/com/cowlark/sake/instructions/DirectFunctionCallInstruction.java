package com.cowlark.sake.instructions;

import com.cowlark.sake.ast.nodes.Node;
import com.cowlark.sake.symbols.Function;

public class DirectFunctionCallInstruction extends Instruction
{
	private Function _function;
	private int _args;
	
	public DirectFunctionCallInstruction(Node node, Function function, int args)
    {
		super(node, args);
		_function = function;
		_args = args;
    }
	
	public Function getFunction()
	{
		return _function;
	}
	
	public int getNumberOfArguments()
	{
		return _args;
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
