package com.cowlark.sake.symbols;

import com.cowlark.sake.ast.nodes.IdentifierNode;
import com.cowlark.sake.ast.nodes.Node;
import com.cowlark.sake.ast.nodes.ScopeNode;
import com.cowlark.sake.types.Type;

public abstract class Symbol implements Comparable<Symbol>
{
	private static int _globalId = 0;
	
	private int _id = _globalId++;
	
	private Node _node;
	private IdentifierNode _name;
	private Type _type;
	private ScopeNode _scope;
	private SymbolStorage _storage;
	
	public Symbol(Node node, IdentifierNode name, Type type)
	{
		_node = node;
		_name = name;
		_type = type;
	}

	public Node getNode()
	{
		return _node;
	}
	
	public IdentifierNode getSymbolName()
	{
		return _name;
	}
	
	public Type getSymbolType()
	{
		return _type;
	}
	
	public void setSymbolType(Type type)
	{
		_type = type;
	}
	
	@Override
	public String toString()
	{
		return super.toString() + ": " + _name.getText() + ": " +
			_type.getCanonicalTypeName();
	}
		
	@Override
	public int compareTo(Symbol other)
	{
		if (_id < other._id)
			return -1;
		if (_id > other._id)
			return 1;
		return 0;
	}

	public void setScopeAndStorage(ScopeNode scope, SymbolStorage storage)
	{
		_scope = scope;
		_storage = storage;
	}
}
