package com.cowlark.sake.instructions;

import com.cowlark.sake.Constructor;
import com.cowlark.sake.ast.nodes.Node;

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
