package com.cowlark.sake.errors;

import java.util.List;
import com.cowlark.sake.ast.nodes.Node;
import com.cowlark.sake.methods.Method;
import com.cowlark.sake.types.Type;

public class MethodParameterMismatch extends CompilationException
{
    private Node _node;
    private Method _method;
	private List<Type> _actual;
	private List<Type> _called;
    
	public MethodParameterMismatch(Node node, Method method,
			List<Type> actual, List<Type> called)
    {
		_node = node;
		_method = method;
		_actual = actual;
		_called = called;
    }
}
