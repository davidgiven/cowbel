package com.cowlark.cowbel.instructions;

import com.cowlark.cowbel.Constructor;
import com.cowlark.cowbel.ast.nodes.Node;

public class ConstructInstruction extends Instruction
{
	private Constructor _constructor;
	
	public ConstructInstruction(Node node, Constructor constructor)
    {
		super(node, 0);
		_constructor = constructor;
    }
	
	public Constructor getConstructor()
	{
		return _constructor;
	}
	
	@Override
	protected String getInstructionName()
	{
	    return "Construct";
	}
	
	@Override
	protected String getShortDescription()
	{
	    return _constructor.toString();
	}
	
	public void visit(InstructionVisitor visitor)
	{
		visitor.visit(this);
	}
}