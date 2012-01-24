package com.cowlark.cowbel.errors;

import java.util.List;
import com.cowlark.cowbel.ast.nodes.Node;
import com.cowlark.cowbel.types.Type;

public class FunctionParameterMismatch extends CompilationException
{
    private Node _node;
	private List<Type> _actual;
	private List<Type> _called;
    
	public FunctionParameterMismatch(Node node, List<Type> actual, List<Type> called)
    {
		_node = node;
		_actual = actual;
		_called = called;
    }
}
