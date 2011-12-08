package com.cowlark.sake.ast.nodes;

import com.cowlark.sake.ast.Visitor;
import com.cowlark.sake.errors.CompilationException;
import com.cowlark.sake.parser.core.Location;
import com.cowlark.sake.types.ListType;
import com.cowlark.sake.types.Type;

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
    protected Type getTypeImpl()
	{
	    return ListType.create(getChildTypeNode().getType());
	}
	
	@Override
	public void visit(Visitor visitor) throws CompilationException
	{
		visitor.visit(this);
	}
}