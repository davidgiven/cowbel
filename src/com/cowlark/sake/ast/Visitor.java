package com.cowlark.sake.ast;

import com.cowlark.sake.ast.nodes.FunctionDefinitionNode;
import com.cowlark.sake.ast.nodes.Node;
import com.cowlark.sake.ast.nodes.StatementListNode;
import com.cowlark.sake.ast.nodes.VarDeclarationNode;
import com.cowlark.sake.errors.CompilationException;

public class Visitor
{
	protected void defaultAction(Node node) throws CompilationException
	{}
	
	public void visit(Node node) throws CompilationException
	{
		defaultAction(node);
	}
	
	public void visit(StatementListNode node) throws CompilationException
	{
		defaultAction(node);
	}
	
	public void visit(FunctionDefinitionNode node) throws CompilationException
	{
		defaultAction(node);
	}

	public void visit(VarDeclarationNode node) throws CompilationException
	{
		defaultAction(node);
	}
}
