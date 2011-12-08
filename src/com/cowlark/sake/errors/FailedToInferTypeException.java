package com.cowlark.sake.errors;

import com.cowlark.sake.ast.nodes.Node;
import com.cowlark.sake.types.InferredType;

public class FailedToInferTypeException extends CompilationException
{
    private static final long serialVersionUID = 1703271215661976233L;
    
    private Node _node;
	private InferredType _type;
    
	public FailedToInferTypeException(Node node, InferredType type)
    {
		_type = type;
    }
}
