package com.cowlark.cowbel.ast;

import com.cowlark.cowbel.ast.nodes.ArrayConstructorNode;
import com.cowlark.cowbel.ast.nodes.BooleanConstantNode;
import com.cowlark.cowbel.ast.nodes.BreakStatementNode;
import com.cowlark.cowbel.ast.nodes.ContinueStatementNode;
import com.cowlark.cowbel.ast.nodes.DirectFunctionCallNode;
import com.cowlark.cowbel.ast.nodes.DoWhileStatementNode;
import com.cowlark.cowbel.ast.nodes.DummyExpressionNode;
import com.cowlark.cowbel.ast.nodes.ExpressionStatementNode;
import com.cowlark.cowbel.ast.nodes.ForStatementNode;
import com.cowlark.cowbel.ast.nodes.FunctionCallNode;
import com.cowlark.cowbel.ast.nodes.FunctionDefinitionNode;
import com.cowlark.cowbel.ast.nodes.FunctionScopeConstructorNode;
import com.cowlark.cowbel.ast.nodes.GotoStatementNode;
import com.cowlark.cowbel.ast.nodes.IfElseStatementNode;
import com.cowlark.cowbel.ast.nodes.IfStatementNode;
import com.cowlark.cowbel.ast.nodes.IntegerConstantNode;
import com.cowlark.cowbel.ast.nodes.LabelStatementNode;
import com.cowlark.cowbel.ast.nodes.MethodCallNode;
import com.cowlark.cowbel.ast.nodes.Node;
import com.cowlark.cowbel.ast.nodes.ReturnStatementNode;
import com.cowlark.cowbel.ast.nodes.ReturnVoidStatementNode;
import com.cowlark.cowbel.ast.nodes.ScopeConstructorNode;
import com.cowlark.cowbel.ast.nodes.StatementListNode;
import com.cowlark.cowbel.ast.nodes.StringConstantNode;
import com.cowlark.cowbel.ast.nodes.VarAssignmentNode;
import com.cowlark.cowbel.ast.nodes.VarDeclarationNode;
import com.cowlark.cowbel.ast.nodes.VarReferenceNode;
import com.cowlark.cowbel.ast.nodes.WhileStatementNode;
import com.cowlark.cowbel.errors.CompilationException;

public class Visitor
{
	public void visit(Node node) throws CompilationException
	{
	}
	
	public void visit(StatementListNode node) throws CompilationException
	{
		visit((Node) node);
	}
	
	public void visit(ScopeConstructorNode node) throws CompilationException
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

	public void visit(ReturnStatementNode node) throws CompilationException
	{
		visit((Node) node);
	}
	
	public void visit(ReturnVoidStatementNode node) throws CompilationException
	{
		visit((Node) node);
	}

	public void visit(BreakStatementNode node) throws CompilationException
	{
		visit((Node) node);
	}

	public void visit(ContinueStatementNode node) throws CompilationException
	{
		visit((Node) node);
	}

	public void visit(IfStatementNode node) throws CompilationException
	{
		visit((Node) node);
	}

	public void visit(IfElseStatementNode node) throws CompilationException
	{
		visit((Node) node);
	}

	public void visit(WhileStatementNode node) throws CompilationException
	{
		visit((Node) node);
	}

	public void visit(DoWhileStatementNode node) throws CompilationException
	{
		visit((Node) node);
	}

	public void visit(ForStatementNode node) throws CompilationException
	{
		visit((Node) node);
	}

	public void visit(LabelStatementNode node) throws CompilationException
	{
		visit((Node) node);
	}

	public void visit(GotoStatementNode node) throws CompilationException
	{
		visit((Node) node);
	}

	public void visit(ExpressionStatementNode node) throws CompilationException
	{
		visit((Node) node);
	}

	public void visit(DummyExpressionNode node) throws CompilationException
	{
		visit((Node) node);
	}
	
	public void visit(VarReferenceNode node) throws CompilationException
	{
		visit((Node) node);
	}
	
	public void visit(DirectFunctionCallNode node) throws CompilationException
	{
		visit((Node) node);
	}
	
	public void visit(FunctionCallNode node) throws CompilationException
	{
		visit((Node) node);
	}
	
	public void visit(BooleanConstantNode node) throws CompilationException
	{
		visit((Node) node);
	}
	
	public void visit(IntegerConstantNode node) throws CompilationException
	{
		visit((Node) node);
	}
	
	public void visit(StringConstantNode node) throws CompilationException
	{
		visit((Node) node);
	}
	
	public void visit(ArrayConstructorNode node) throws CompilationException
	{
		visit((Node) node);
	}
	
	public void visit(MethodCallNode node) throws CompilationException
	{
		visit((Node) node);
	}
}
