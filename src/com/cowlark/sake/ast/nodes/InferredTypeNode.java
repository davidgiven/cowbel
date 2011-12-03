package com.cowlark.sake.ast.nodes;

import com.cowlark.sake.ast.Visitor;
import com.cowlark.sake.errors.CompilationException;
import com.cowlark.sake.parser.core.Location;
import com.cowlark.sake.types.InferredType;
import com.cowlark.sake.types.Type;

public class InferredTypeNode extends TypeNode
{
	private ExpressionNode _initialiser;
	private static Type _type = new InferredType();
	
	public InferredTypeNode(Location start, Location end,
			ExpressionNode initialiser)
    {
        super(start, end);
        _initialiser = initialiser;
    }
	
	@Override
	public Type constructTypeObject()
	{
		return _type;
	}
	
	@Override
	public void visit(Visitor visitor) throws CompilationException
	{
		visitor.visit(this);
	}
	
	public ExpressionNode getInitialiser()
	{
		return _initialiser;
	}
}