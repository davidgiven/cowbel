package com.cowlark.cowbel;

import com.cowlark.cowbel.ast.SimpleVisitor;
import com.cowlark.cowbel.ast.nodes.BreakStatementNode;
import com.cowlark.cowbel.ast.nodes.ContinueStatementNode;
import com.cowlark.cowbel.ast.nodes.DoWhileStatementNode;
import com.cowlark.cowbel.ast.nodes.ExpressionNode;
import com.cowlark.cowbel.ast.nodes.ExpressionStatementNode;
import com.cowlark.cowbel.ast.nodes.ForStatementNode;
import com.cowlark.cowbel.ast.nodes.FunctionDefinitionNode;
import com.cowlark.cowbel.ast.nodes.GotoStatementNode;
import com.cowlark.cowbel.ast.nodes.IfElseStatementNode;
import com.cowlark.cowbel.ast.nodes.IfStatementNode;
import com.cowlark.cowbel.ast.nodes.LabelStatementNode;
import com.cowlark.cowbel.ast.nodes.Node;
import com.cowlark.cowbel.ast.nodes.ReturnStatementNode;
import com.cowlark.cowbel.ast.nodes.ReturnVoidStatementNode;
import com.cowlark.cowbel.ast.nodes.ScopeConstructorNode;
import com.cowlark.cowbel.ast.nodes.StatementNode;
import com.cowlark.cowbel.ast.nodes.VarAssignmentNode;
import com.cowlark.cowbel.ast.nodes.VarDeclarationNode;
import com.cowlark.cowbel.ast.nodes.WhileStatementNode;
import com.cowlark.cowbel.errors.CompilationException;
import com.cowlark.cowbel.symbols.Function;
import com.cowlark.cowbel.symbols.Symbol;
import com.cowlark.cowbel.types.BooleanType;
import com.cowlark.cowbel.types.FunctionType;
import com.cowlark.cowbel.types.Type;
import com.cowlark.cowbel.types.VoidType;

public class CheckAndInferStatementTypesVisitor extends SimpleVisitor
{
	@Override
	public void visit(FunctionDefinitionNode node) throws CompilationException
	{
	}
	
	@Override
	public void visit(ScopeConstructorNode node) throws CompilationException
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
		
		Function function = node.getScope().getFunctionScope().getFunction();
		FunctionType functiontype = (FunctionType) function.getSymbolType();
		Type returntype = functiontype.getReturnType();
		
		valuetype.unifyWith(node, returntype);
		valuetype.ensureConcrete(node);
	}

	@Override
	public void visit(ReturnVoidStatementNode node) throws CompilationException
	{
		Type valuetype = VoidType.create();
		
		Function function = node.getScope().getFunctionScope().getFunction();
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
	public void visit(WhileStatementNode node) throws CompilationException
	{
		ExpressionNode conditional = node.getConditionalExpression();
		Type conditionaltype = conditional.calculateType();
		conditionaltype.unifyWith(node, BooleanType.create());
		
		StatementNode body = node.getBodyStatement();
		body.checkTypes();
	}

	@Override
	public void visit(DoWhileStatementNode node) throws CompilationException
	{
		ExpressionNode conditional = node.getConditionalExpression();
		Type conditionaltype = conditional.calculateType();
		conditionaltype.unifyWith(node, BooleanType.create());
		
		StatementNode body = node.getBodyStatement();
		body.checkTypes();
	}

	@Override
	public void visit(ForStatementNode node) throws CompilationException
	{
		node.getInitialiserStatement().checkTypes();
		
		ExpressionNode conditional = node.getConditionalExpression();
		Type conditionaltype = conditional.calculateType();
		conditionaltype.unifyWith(conditional, BooleanType.create());
		
		node.getIncrementerStatement().checkTypes();
		node.getBodyStatement().checkTypes();
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
	public void visit(BreakStatementNode node) throws CompilationException
	{
	}
	
	@Override
	public void visit(ContinueStatementNode node) throws CompilationException
	{
	}
	
	@Override
	public void visit(Node node) throws CompilationException
	{
		assert(false);
	}
}
