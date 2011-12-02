package com.cowlark.sake;

import com.cowlark.sake.ast.nodes.IdentifierNode;
import com.cowlark.sake.ast.nodes.ScopeNode;

public abstract class Symbol
{
	private IdentifierNode _name;
	private ScopeNode _scope;
	private SymbolStorage _storage;
	
	public Symbol(IdentifierNode name)
	{
		_name = name;
	}
	
	public IdentifierNode getSymbolName()
	{
		return _name;
	}
	
	public void setScopeAndStorage(ScopeNode scope, SymbolStorage storage)
	{
		_scope = scope;
		_storage = storage;
	}
}
