package com.cowlark.cowbel.ast.nodes;

import com.cowlark.cowbel.ast.Visitor;
import com.cowlark.cowbel.errors.CompilationException;
import com.cowlark.cowbel.parser.core.Location;
import com.cowlark.cowbel.types.Type;
import com.cowlark.cowbel.types.TypeVariable;

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
	public String getShortDescription()
	{
		return getType().toString();
	}
	
	@Override
	public void visit(Visitor visitor) throws CompilationException
	{
		visitor.visit(this);
	}
}