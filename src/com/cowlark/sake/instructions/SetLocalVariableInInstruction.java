package com.cowlark.sake.instructions;

import com.cowlark.sake.BasicBlock;
import com.cowlark.sake.LocalVariable;
import com.cowlark.sake.ast.nodes.Node;

public class SetLocalVariableInInstruction extends Instruction
{
	private LocalVariable _var;
	private BasicBlock _next;
	
	public SetLocalVariableInInstruction(Node node, LocalVariable var, BasicBlock next)
    {
		super(node, 1);
		_var = var;
		_next = next;
    }
	
	@Override
	protected String getInstructionName()
	{
	    return "SetLocalVariableIn";
	}
	
	@Override
	protected String getShortDescription()
	{
	    return _var.getSymbolName().getText() + " in " + _next;
	}

	@Override
	public String toString()
	{
		return "SetLocalVariableIn " + _var.getSymbolName().getText() + " " + _next;
	}
}
