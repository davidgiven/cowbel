package com.cowlark.sake.ast;

import com.cowlark.sake.symbols.Symbol;

public interface HasSymbol
{
	public void setSymbol(Symbol symbol);
	public Symbol getSymbol();
}
