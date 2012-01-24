package com.cowlark.cowbel.instructions;

import com.cowlark.cowbel.Constructor;
import com.cowlark.cowbel.ast.nodes.Node;
import com.cowlark.cowbel.symbols.Variable;

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
	    return "SetUpvalue";
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
