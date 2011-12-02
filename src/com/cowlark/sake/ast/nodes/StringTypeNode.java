package com.cowlark.sake.ast.nodes;

import com.cowlark.sake.ast.Visitor;
import com.cowlark.sake.errors.CompilationException;
import com.cowlark.sake.parser.core.Location;

public class StringTypeNode extends TypeNode
{
	public StringTypeNode(Location start, Location end)
    {
        super(start, end);
    }
	
	@Override
	public String getCanonicalNameOfType()
	{
	    return "string";
	}
	
	@Override
	public void visit(Visitor visitor) throws CompilationException
	{
		visitor.visit(this);
	}
}