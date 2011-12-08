package com.cowlark.sake.errors;

import com.cowlark.sake.ast.nodes.Node;

public class AttemptToCallNonFunctionTypeException extends CompilationException
{
    private static final long serialVersionUID = -6195443274161105426L;
    
	private Node _node;
	
	public AttemptToCallNonFunctionTypeException(Node node)
    {
		_node = node;
    }
}
