package com.cowlark.sake.instructions;

import com.cowlark.sake.GlobalVariable;
import com.cowlark.sake.ast.nodes.Node;

public class SetGlobalVariableInstruction extends Instruction
{
	private GlobalVariable _var;
	
	public SetGlobalVariableInstruction(Node node, GlobalVariable var)
	{
		super(node, 1);
		_var = var;
	}	
	
	@Override
	protected String getInstructionName()
	{
	    return "SetGlobalVariable";
	}
	
	@Override
	protected String getShortDescription()
	{
	    return _var.getSymbolName().getText();
	}
}
