package com.cowlark.sake.ast.nodes;

import java.util.List;
import com.cowlark.sake.ast.Visitor;
import com.cowlark.sake.errors.CompilationException;
import com.cowlark.sake.parser.core.Location;

public class FunctionCallNode extends ExpressionNode
{
	public FunctionCallNode(Location start, Location end, ExpressionNode object,
			ExpressionNode... arguments)
    {
		super(start, end);
		addChild(object);
		for (ExpressionNode n : arguments)
			addChild(n);
    }
	
	public FunctionCallNode(Location start, Location end, ExpressionNode object,
			List<ExpressionNode> arguments)
	{
		super(start, end);
		addChild(object);
		for (ExpressionNode n : arguments)
			addChild(n);
	}
	
	public ExpressionNode getFunction()
	{
		return (ExpressionNode) getChild(0);
	}
	
	@Override
	public void visit(Visitor visitor) throws CompilationException
	{
		visitor.visit(this);
	}
}
