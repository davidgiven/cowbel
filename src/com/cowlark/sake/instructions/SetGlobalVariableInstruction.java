package com.cowlark.sake.instructions;

import com.cowlark.sake.ast.nodes.Node;
import com.cowlark.sake.symbols.GlobalVariable;

public class SetGlobalVariableInstruction extends Instruction
{
	private GlobalVariable _var;
	
	public SetGlobalVariableInstruction(Node node, GlobalVariable var)
	{
		super(node, 1);
		_var = var;
	}	
	
	public GlobalVariable getVariable()
	{
		return _var;
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
	
	public void visit(InstructionVisitor visitor)
	{
		visitor.visit(this);
	}
}
