package com.cowlark.sake.ast.nodes;

import com.cowlark.sake.CompilationException;
import com.cowlark.sake.ast.Visitor;
import com.cowlark.sake.parser.core.Location;

public class InferredTypeNode extends TypeNode
{
	public InferredTypeNode(Location start, Location end)
    {
        super(start, end);
    }
	
	@Override
	public String getCanonicalNameOfType()
	{
	    assert(false);
	    return null;
	}
	
	@Override
	public void visit(Visitor visitor) throws CompilationException
	{
		visitor.visit(this);
	}
}