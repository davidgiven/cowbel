package com.cowlark.cowbel.ast;

import com.cowlark.cowbel.ast.nodes.IdentifierListNode;

public interface IsCallableStatement extends IsCallable
{
	public IdentifierListNode getVariables();
}
