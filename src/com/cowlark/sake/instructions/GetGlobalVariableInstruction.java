package com.cowlark.sake.instructions;

import com.cowlark.sake.GlobalVariable;
import com.cowlark.sake.ast.nodes.Node;

public class GetGlobalVariableInstruction extends Instruction
{
	private GlobalVariable _var;
	
	public GetGlobalVariableInstruction(Node node, GlobalVariable var)
	{
		super(node, 0);
		_var = var;
	}	
	
	@Override
	protected String getInstructionName()
	{
	    return "GetGlobalVariable";
	}
	
	@Override
	protected String getShortDescription()
	{
		return _var.getSymbolName().getText();
	}
}
