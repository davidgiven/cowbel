package com.cowlark.cowbel.ast;

import com.cowlark.cowbel.ast.nodes.Node;
import com.cowlark.cowbel.errors.CompilationException;

public class RecursiveVisitor extends Visitor
{
	@Override
	public void visit(Node node) throws CompilationException
	{
		for (Node n : node.getChildren())
			n.visit(this);
	}
}
