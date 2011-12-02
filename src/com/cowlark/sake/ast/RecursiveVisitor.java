package com.cowlark.sake.ast;

import com.cowlark.sake.ast.nodes.Node;
import com.cowlark.sake.errors.CompilationException;

public class RecursiveVisitor extends Visitor
{
	@Override
	protected void defaultAction(Node node) throws CompilationException
	{
		for (Node n : node.getChildren())
			n.visit(this);
	}
}
