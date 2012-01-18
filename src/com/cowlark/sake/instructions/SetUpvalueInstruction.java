package com.cowlark.sake.instructions;

import com.cowlark.sake.Constructor;
import com.cowlark.sake.ast.nodes.Node;
import com.cowlark.sake.symbols.Variable;

public class SetUpvalueInstruction extends Instruction
{
	private Variable _var;
	private Constructor _constructor;
	
	public SetUpvalueInstruction(Node node, Constructor c, Variable var)
	{
		super(node, 1);
		_constructor = c;
		_var = var;
	}	
	
	public Variable getVariable()
	{
		return _var;
	}

	@Override
	protected String getInstructionName()
	{
	    return "SetUpvale";
	}
	
	@Override
	protected String getShortDescription()
	{
	    return _constructor.toString() + ": " + _var.getSymbolName().getText();
	}
	
	public void visit(InstructionVisitor visitor)
	{
		visitor.visit(this);
	}
}
