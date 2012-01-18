package com.cowlark.sake.instructions;

import com.cowlark.sake.ast.nodes.Node;
import com.cowlark.sake.symbols.Variable;

public class GetLocalInstruction extends Instruction
{
	private Variable _var;
	
	public GetLocalInstruction(Node node, Variable var)
	{
		super(node, 0);
		_var = var;
	}	
	
	public Variable getVariable()
	{
		return _var;
	}
	
	@Override
	protected String getInstructionName()
	{
	    return "GetLocal";
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
