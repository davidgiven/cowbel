package com.cowlark.sake;

import java.util.HashSet;

public abstract class SymbolStorage
{
	private HashSet<Symbol> _symbols = new HashSet<Symbol>();
	
	public SymbolStorage()
    {
    }
	
	public void addSymbol(Symbol symbol)
	{
		_symbols.add(symbol);
	}
}
