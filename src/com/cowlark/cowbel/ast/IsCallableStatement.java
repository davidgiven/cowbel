package com.cowlark.cowbel.ast;

import com.cowlark.cowbel.ast.nodes.IdentifierListNode;
import com.cowlark.cowbel.symbols.Symbol;

public interface IsCallableStatement extends IsCallable
{
	public IdentifierListNode getVariables();
}
