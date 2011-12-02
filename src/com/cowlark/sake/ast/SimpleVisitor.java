package com.cowlark.sake.ast;

import com.cowlark.sake.CompilationException;
import com.cowlark.sake.ast.nodes.Node;
import com.cowlark.sake.ast.nodes.StatementListNode;

public class SimpleVisitor extends Visitor
{
	@Override
	public void visit(StatementListNode node) throws CompilationException
	{
		for (Node n : node.getChildren())
			n.visit(this);
	}
	
	@Override
	public void visit(Node node) throws CompilationException
	{
	}
}
