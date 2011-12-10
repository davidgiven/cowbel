package com.cowlark.sake.ast.nodes;

import com.cowlark.sake.ast.Visitor;
import com.cowlark.sake.errors.CompilationException;
import com.cowlark.sake.parser.core.Location;

public class DummyExpressionNode extends ExpressionNode
{
	public DummyExpressionNode(Location start, Location end, ExpressionNode child)
    {
        super(start, end);
        addChild(child);
    }
	
	public ExpressionNode getChild()
	{
		return (ExpressionNode) getChild(0);
	}
	
	@Override
	public void visit(Visitor visitor) throws CompilationException
	{
		visitor.visit(this);
	}
}