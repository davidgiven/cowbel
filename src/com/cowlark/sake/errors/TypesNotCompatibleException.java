package com.cowlark.sake.errors;

import com.cowlark.sake.ast.nodes.Node;
import com.cowlark.sake.types.Type;

public class TypesNotCompatibleException extends CompilationException
{
    private static final long serialVersionUID = 1703271215661976233L;
    
    private Node _node;
	private Type _type1;
	private Type _type2;
    
	public TypesNotCompatibleException(Node node, Type type1, Type type2)
    {
		_node = node;
		_type1 = type1;
		_type2 = type2;
    }
}
