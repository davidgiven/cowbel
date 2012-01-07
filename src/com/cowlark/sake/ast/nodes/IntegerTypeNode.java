package com.cowlark.sake.ast.nodes;

import com.cowlark.sake.ast.Visitor;
import com.cowlark.sake.errors.CompilationException;
import com.cowlark.sake.parser.core.Location;
import com.cowlark.sake.types.IntegerType;
import com.cowlark.sake.types.Type;

public class IntegerTypeNode extends TypeNode
{
	public IntegerTypeNode(Location start, Location end)
    {
        super(start, end);
    }
	
	@Override
    protected Type getTypeImpl()
	{
	    return IntegerType.create();
	}
	
	@Override
	public void visit(Visitor visitor) throws CompilationException
	{
		visitor.visit(this);
	}
}