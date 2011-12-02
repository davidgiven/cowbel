package com.cowlark.sake.ast.nodes;

import com.cowlark.sake.ast.Visitor;
import com.cowlark.sake.errors.CompilationException;
import com.cowlark.sake.parser.core.Location;

public class ListTypeNode extends TypeNode
{
	public ListTypeNode(Location start, Location end, TypeNode childpr)
    {
        super(start, end);
        addChild(childpr);
    }
	
	public TypeNode getChildTypeNode()
	{
		return (TypeNode) getChild(0);
	}
	
	@Override
	public String getCanonicalNameOfType()
	{
	    return "[" + getChildTypeNode().getCanonicalNameOfType() + "]";
	}
	
	@Override
	public void visit(Visitor visitor) throws CompilationException
	{
		visitor.visit(this);
	}
}