package com.cowlark.sake.instructions;

import com.cowlark.sake.Constructor;
import com.cowlark.sake.ast.nodes.Node;
import com.cowlark.sake.symbols.Variable;

public class GetUpvalueInstruction extends Instruction
{
	private Constructor _constructor;
	private Variable _var;
	
	public GetUpvalueInstruction(Node node, Constructor c, Variable var)
	{
		super(node, 0);
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
	    return "GetUpvalue";
	}
	
	@Override
	protected String getShortDescription()
	{
		return _constructor + ": " + _var.getSymbolName().getText();
	}
	
	public void visit(InstructionVisitor visitor)
	{
		visitor.visit(this);
	}
}