package com.cowlark.sake.ast.nodes;

import com.cowlark.sake.ast.Visitor;
import com.cowlark.sake.errors.CompilationException;
import com.cowlark.sake.parser.core.Location;
import com.cowlark.sake.types.TypeVariable;
import com.cowlark.sake.types.Type;

public class InferredTypeNode extends TypeNode
{
	public InferredTypeNode(Location start, Location end)
    {
        super(start, end);
    }
	
	@Override
    protected Type getTypeImpl()
	{
		return TypeVariable.create();
	}
	
	@Override
	public void visit(Visitor visitor) throws CompilationException
	{
		visitor.visit(this);
	}
}