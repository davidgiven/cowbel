package com.cowlark.cowbel.ast;

import java.util.List;
import com.cowlark.cowbel.ast.nodes.ExpressionNode;
import com.cowlark.cowbel.types.Type;

public interface IsCallable
{
	public List<ExpressionNode> getArguments();
	public int getArgumentCount();
}
