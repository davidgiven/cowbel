package com.cowlark.cowbel.ast.nodes;

import com.cowlark.cowbel.ast.Visitor;
import com.cowlark.cowbel.errors.CompilationException;
import com.cowlark.cowbel.parser.core.Location;

public class IndirectFunctionCallExpressionNode extends ExpressionNode
{
	public IndirectFunctionCallExpressionNode(Location start, Location end,
			ExpressionNode object, ArgumentListNode arguments)
    {
		super(start, end);
		addChild(object);
		addChild(arguments);
    }
	
	public ExpressionNode getFunction()
	{
		return (ExpressionNode) getChild(0);
	}

	public ArgumentListNode getArguments()
	{
		return (ArgumentListNode) getChild(1);
	}
	
	@Override
	public void visit(Visitor visitor) throws CompilationException
	{
		visitor.visit(this);
	}
}
