package com.cowlark.sake.errors;

import com.cowlark.sake.ast.nodes.IdentifierNode;
import com.cowlark.sake.ast.nodes.Node;
import com.cowlark.sake.types.Type;

public class NoSuchMethodException extends CompilationException
{
	private Node _node;
	private Type _type;
	private IdentifierNode _id;
	
	public NoSuchMethodException(Node node, Type type, IdentifierNode id)
    {
		_node = node;
		_type = type;
		_id = id;
    }
	
	@Override
	public String toString()
	{
		return "No such method '" + _id.getText() + "' on value of type " +
			_type.getCanonicalTypeName() + " at " + _node.toString();
	}
}
