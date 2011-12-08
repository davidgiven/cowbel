package com.cowlark.sake.types;

import com.cowlark.sake.ast.nodes.ExpressionNode;
import com.cowlark.sake.ast.nodes.Node;
import com.cowlark.sake.errors.CompilationException;
import com.cowlark.sake.errors.FailedToInferTypeException;

public class InferredType extends Type
{
	public static InferredType create(ExpressionNode initialiser)
	{
		return new InferredType(initialiser);
	}
	
	private ExpressionNode _initialiser;
	
	private InferredType(ExpressionNode initialiser)
    {
		_initialiser = initialiser;
    }
	
	@Override
	public String getCanonicalTypeName()
	{
	    return "inferred";
	}
	
	public boolean canEquivalentTypesBeCollapsed()
	{
		return false;
	}
	
	public ExpressionNode getInitialiser()
	{
		return _initialiser;
	}
	
	public void checkType(Node node) throws CompilationException
	{
		throw new FailedToInferTypeException(node, this);
	}
}
