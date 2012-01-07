package com.cowlark.sake;

import com.cowlark.sake.ast.SimpleVisitor;
import com.cowlark.sake.ast.nodes.ExpressionNode;
import com.cowlark.sake.ast.nodes.ExpressionStatementNode;
import com.cowlark.sake.ast.nodes.FunctionDefinitionNode;
import com.cowlark.sake.ast.nodes.GotoStatementNode;
import com.cowlark.sake.ast.nodes.IfElseStatementNode;
import com.cowlark.sake.ast.nodes.IfStatementNode;
import com.cowlark.sake.ast.nodes.LabelStatementNode;
import com.cowlark.sake.ast.nodes.Node;
import com.cowlark.sake.ast.nodes.ReturnStatementNode;
import com.cowlark.sake.ast.nodes.ReturnVoidStatementNode;
import com.cowlark.sake.ast.nodes.ScopeNode;
import com.cowlark.sake.ast.nodes.StatementNode;
import com.cowlark.sake.ast.nodes.VarAssignmentNode;
import com.cowlark.sake.ast.nodes.VarDeclarationNode;
import com.cowlark.sake.errors.CompilationException;
import com.cowlark.sake.symbols.Function;
import com.cowlark.sake.symbols.LocalSymbolStorage;
import com.cowlark.sake.symbols.Symbol;
import com.cowlark.sake.types.BooleanType;
import com.cowlark.sake.types.FunctionType;
import com.cowlark.sake.types.Type;
import com.cowlark.sake.types.VoidType;

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
		Type symboltype = symbol.getSymbolType();
		
		ExpressionNode initialiser = node.getVariableInitialiser();
		if (initialiser != null)
			symboltype.unifyWith(node, initialiser.calculateType());
		
		symboltype.ensureConcrete(node);
	}
	
	@Override
	public void visit(VarAssignmentNode node) throws CompilationException
	{
		Symbol symbol = node.getSymbol();
		Type symboltype = symbol.getSymbolType();
		ExpressionNode expression = node.getExpression();
		Type expressiontype = expression.calculateType();

		symboltype.unifyWith(node, expressiontype);
		symboltype.ensureConcrete(node);
	}
	
	@Override
	public void visit(ExpressionStatementNode node) throws CompilationException
	{
		Type type = node.getExpression().calculateType();
		type.ensureConcrete(node);
	}
	
	@Override
	public void visit(ReturnStatementNode node) throws CompilationException
	{
		ExpressionNode value = node.getValue();
		Type valuetype = value.calculateType();
		
		LocalSymbolStorage storage = (LocalSymbolStorage) node.getScope().getSymbolStorage();
		Function function = storage.getDefiningFunction();
		FunctionType functiontype = (FunctionType) function.getSymbolType();
		Type returntype = functiontype.getReturnType();
		
		valuetype.unifyWith(node, returntype);
		valuetype.ensureConcrete(node);
	}

	@Override
	public void visit(ReturnVoidStatementNode node) throws CompilationException
	{
		Type valuetype = VoidType.create();
		
		LocalSymbolStorage storage = (LocalSymbolStorage) node.getScope().getSymbolStorage();
		Function function = storage.getDefiningFunction();
		FunctionType functiontype = (FunctionType) function.getSymbolType();
		Type returntype = functiontype.getReturnType();
		
		valuetype.unifyWith(node, returntype);
	}

	@Override
	public void visit(IfStatementNode node) throws CompilationException
	{
		ExpressionNode conditional = node.getConditionalExpression();
		Type conditionaltype = conditional.calculateType();
		conditionaltype.unifyWith(node, BooleanType.create());
		
		StatementNode positive = node.getPositiveStatement();
		positive.checkTypes();
	}

	@Override
	public void visit(IfElseStatementNode node) throws CompilationException
	{
		ExpressionNode conditional = node.getConditionalExpression();
		Type conditionaltype = conditional.calculateType();
		conditionaltype.unifyWith(node, BooleanType.create());
		
		StatementNode positive = node.getPositiveStatement();
		positive.checkTypes();
		
		StatementNode negative = node.getNegativeStatement();
		negative.checkTypes();
	}

	@Override
	public void visit(LabelStatementNode node) throws CompilationException
	{
	}
	
	@Override
	public void visit(GotoStatementNode node) throws CompilationException
	{
	}
	
	@Override
	public void visit(Node node) throws CompilationException
	{
		assert(false);
	}
}
