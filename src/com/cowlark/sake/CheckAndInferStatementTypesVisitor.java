package com.cowlark.sake;

import com.cowlark.sake.ast.SimpleVisitor;
import com.cowlark.sake.ast.nodes.ExpressionNode;
import com.cowlark.sake.ast.nodes.ExpressionStatementNode;
import com.cowlark.sake.ast.nodes.FunctionDefinitionNode;
import com.cowlark.sake.ast.nodes.Node;
import com.cowlark.sake.ast.nodes.ScopeNode;
import com.cowlark.sake.ast.nodes.VarAssignmentNode;
import com.cowlark.sake.ast.nodes.VarDeclarationNode;
import com.cowlark.sake.errors.CompilationException;
import com.cowlark.sake.types.Type;

public class CheckAndInferStatementTypesVisitor extends SimpleVisitor
{
	@Override
	public void visit(FunctionDefinitionNode node) throws CompilationException
	{
	}
	
	@Override
	public void visit(ScopeNode node) throws CompilationException
	{
		node.getChild().visit(this);
	}
	
	@Override
	public void visit(VarDeclarationNode node) throws CompilationException
	{
		Symbol symbol = node.getSymbol();
		symbol.inferType();
	}
	
	@Override
	public void visit(VarAssignmentNode node) throws CompilationException
	{
		Symbol symbol = node.getSymbol();
		ExpressionNode expression = node.getExpression();
		Type expressiontype = expression.calculateType();

		symbol.getSymbolType().checkCompatibilityWith(node, expressiontype);
	}
	
	@Override
	public void visit(ExpressionStatementNode node) throws CompilationException
	{
		node.getExpression().calculateType();
	}
	
	@Override
	public void visit(Node node) throws CompilationException
	{
		assert(false);
	}
}
