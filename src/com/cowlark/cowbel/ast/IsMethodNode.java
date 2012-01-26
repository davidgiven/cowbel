package com.cowlark.cowbel.ast;

import com.cowlark.cowbel.ast.nodes.ExpressionNode;
import com.cowlark.cowbel.ast.nodes.IdentifierNode;

public interface IsMethodNode extends IsCallable
{
	public IdentifierNode getMethodIdentifier();
	public ExpressionNode getMethodReceiver();
}
