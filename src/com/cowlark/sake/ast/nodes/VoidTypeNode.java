package com.cowlark.sake.ast.nodes;

import com.cowlark.sake.ast.Visitor;
import com.cowlark.sake.errors.CompilationException;
import com.cowlark.sake.parser.core.Location;

public class VoidTypeNode extends TypeNode
{
	public VoidTypeNode(Location start, Location end)
    {
        super(start, end);
    }
	
	@Override
	public String getCanonicalNameOfType()
	{
	    return "void";
	}
	
	@Override
	public void visit(Visitor visitor) throws CompilationException
	{
		visitor.visit(this);
	}
}