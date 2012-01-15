package com.cowlark.sake.symbols;

import java.util.TreeSet;

public abstract class SymbolStorage
{
	private TreeSet<Symbol> _symbols = new TreeSet<Symbol>();
	
	public SymbolStorage()
    {
    }
	
	public void addSymbol(Symbol symbol)
	{
		_symbols.add(symbol);
	}
}
