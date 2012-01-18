package com.cowlark.sake.ast;

import java.util.List;
import com.cowlark.sake.ast.nodes.ExpressionNode;
import com.cowlark.sake.types.Type;

public interface IsCallable
{
	public List<ExpressionNode> getArguments();
	public int getArgumentCount();
}
