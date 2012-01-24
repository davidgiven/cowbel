package com.cowlark.cowbel.instructions;

import com.cowlark.cowbel.ast.nodes.IdentifierNode;
import com.cowlark.cowbel.ast.nodes.Node;

public class MethodCallInstruction extends Instruction
{
	private IdentifierNode _method;
	private int _args;
	
	public MethodCallInstruction(Node node, IdentifierNode method, int args)
    {
		super(node, args+1);
		_method = method;
		_args = args;
    }
	
	public IdentifierNode getMethodName()
	{
		return _method;
	}
	
	public int getNumberOfArguments()
	{
		return _args;
	}
	
	@Override
	protected String getInstructionName()
	{
	    return "MethodCall";
	}
	
	@Override
	protected String getShortDescription()
	{
	    return _method.getText();
	}
	
	public void visit(InstructionVisitor visitor)
	{
		visitor.visit(this);
	}
}
