package com.cowlark.sake.ast.nodes;

import com.cowlark.sake.ast.Visitor;
import com.cowlark.sake.errors.CompilationException;
import com.cowlark.sake.parser.core.Location;
import com.cowlark.sake.types.Type;

public class ExpressionNode extends Node
{
	private Type _type;
	
	public ExpressionNode(Location start, Location end)
    {
        super(start, end);
    }
	
	@Override
	public void visit(Visitor visitor) throws CompilationException
	{
		visitor.visit(this);
	}
	
	public void typeCheck() throws CompilationException
	{
	}
	
	public Type getType()
	{
		return _type;
	}
}