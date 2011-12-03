package com.cowlark.sake.ast.nodes;

import com.cowlark.sake.ast.Visitor;
import com.cowlark.sake.errors.CompilationException;
import com.cowlark.sake.parser.core.Location;
import com.cowlark.sake.types.Type;
import com.cowlark.sake.types.TypeRegistry;

public abstract class TypeNode extends Node
{
	private Type _type;
	
	public TypeNode(Location start, Location end)
    {
        super(start, end);
    }
	
	public Type getType()
	{
		if (_type == null)
			_type = TypeRegistry.canonicalise(constructTypeObject());

		return _type;
	}
	
	public abstract Type constructTypeObject();
	
	@Override
	public void visit(Visitor visitor) throws CompilationException
	{
		visitor.visit(this);
	}
}