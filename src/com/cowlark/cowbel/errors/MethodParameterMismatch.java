package com.cowlark.cowbel.errors;

import java.util.List;
import com.cowlark.cowbel.ast.nodes.Node;
import com.cowlark.cowbel.methods.Method;
import com.cowlark.cowbel.types.Type;

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
