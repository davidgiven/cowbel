package com.cowlark.sake.instructions;

import com.cowlark.sake.LocalVariable;
import com.cowlark.sake.ast.nodes.Node;

public class GetLocalVariableInstruction extends Instruction
{
	private LocalVariable _var;
	
	public GetLocalVariableInstruction(Node node, LocalVariable var)
	{
		super(node, 0);
		_var = var;
	}	
	
	public LocalVariable getVariable()
	{
		return _var;
	}
	
	@Override
	protected String getInstructionName()
	{
	    return "GetLocalVariable";
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
