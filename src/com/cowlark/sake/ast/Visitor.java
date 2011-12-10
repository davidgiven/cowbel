package com.cowlark.sake.ast;

import com.cowlark.sake.ast.nodes.ExpressionStatementNode;
import com.cowlark.sake.ast.nodes.FunctionCallNode;
import com.cowlark.sake.ast.nodes.FunctionDefinitionNode;
import com.cowlark.sake.ast.nodes.ListConstructorNode;
import com.cowlark.sake.ast.nodes.MethodCallNode;
import com.cowlark.sake.ast.nodes.Node;
import com.cowlark.sake.ast.nodes.ScopeNode;
import com.cowlark.sake.ast.nodes.StatementListNode;
import com.cowlark.sake.ast.nodes.StringConstantNode;
import com.cowlark.sake.ast.nodes.VarAssignmentNode;
import com.cowlark.sake.ast.nodes.VarDeclarationNode;
import com.cowlark.sake.ast.nodes.VarReferenceNode;
import com.cowlark.sake.errors.CompilationException;

public class Visitor
{
	public void visit(Node node) throws CompilationException
	{
	}
	
	public void visit(StatementListNode node) throws CompilationException
	{
		visit((Node) node);
	}
	
	public void visit(ScopeNode node) throws CompilationException
	{
		visit((Node) node);
	}
		
	public void visit(FunctionDefinitionNode node) throws CompilationException
	{
		visit((Node) node);
	}

	public void visit(VarDeclarationNode node) throws CompilationException
	{
		visit((Node) node);
	}

	public void visit(VarAssignmentNode node) throws CompilationException
	{
		visit((Node) node);
	}

	public void visit(ExpressionStatementNode node) throws CompilationException
	{
		visit((Node) node);
	}

	public void visit(VarReferenceNode node) throws CompilationException
	{
		visit((Node) node);
	}
	
	public void visit(FunctionCallNode node) throws CompilationException
	{
		visit((Node) node);
	}
	
	public void visit(StringConstantNode node) throws CompilationException
	{
		visit((Node) node);
	}
	
	public void visit(ListConstructorNode node) throws CompilationException
	{
		visit((Node) node);
	}
	
	public void visit(MethodCallNode node) throws CompilationException
	{
		visit((Node) node);
	}
}
