package com.cowlark.sake.instructions;

import com.cowlark.sake.ast.nodes.Node;
import com.cowlark.sake.symbols.Variable;

public class SetLocalInstruction extends Instruction
{
	private Variable _var;
	
	public SetLocalInstruction(Node node, Variable var)
    {
		super(node, 1);
		_var = var;
    }
	
	public Variable getVariable()
	{
		return _var;
	}
	
	@Override
	protected String getInstructionName()
	{
	    return "SetLocal";
	}
	
	@Override
	protected String getShortDescription()
	{
	    return _var.getSymbolName().getText();
	}

	@Override
	public String toString()
	{
		return "SetLocal " + _var.getSymbolName().getText();
	}
	
	public void visit(InstructionVisitor visitor)
	{
		visitor.visit(this);
	}
}
