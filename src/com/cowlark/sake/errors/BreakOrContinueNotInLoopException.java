package com.cowlark.sake.errors;

import com.cowlark.sake.ast.nodes.Node;

public class BreakOrContinueNotInLoopException extends CompilationException
{
    private static final long serialVersionUID = -4324070946445612103L;
    
	private Node _node;
    
	public BreakOrContinueNotInLoopException(Node node)
    {
		_node = node;
    }
}
