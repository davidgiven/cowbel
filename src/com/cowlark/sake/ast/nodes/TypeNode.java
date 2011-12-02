package com.cowlark.sake.ast.nodes;

import com.cowlark.sake.ast.Visitor;
import com.cowlark.sake.errors.CompilationException;
import com.cowlark.sake.parser.core.Location;

public abstract class TypeNode extends Node
{
	public TypeNode(Location start, Location end)
    {
        super(start, end);
    }
	
	public abstract String getCanonicalNameOfType();
	
	@Override
	public void visit(Visitor visitor) throws CompilationException
	{
		visitor.visit(this);
	}
}