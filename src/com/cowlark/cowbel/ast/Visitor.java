/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING.BSD in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel.ast;

import com.cowlark.cowbel.ast.nodes.BlockExpressionNode;
import com.cowlark.cowbel.ast.nodes.BlockScopeConstructorNode;
import com.cowlark.cowbel.ast.nodes.BooleanConstantNode;
import com.cowlark.cowbel.ast.nodes.BreakStatementNode;
import com.cowlark.cowbel.ast.nodes.ContinueStatementNode;
import com.cowlark.cowbel.ast.nodes.DirectFunctionCallExpressionNode;
import com.cowlark.cowbel.ast.nodes.DirectFunctionCallStatementNode;
import com.cowlark.cowbel.ast.nodes.DoWhileStatementNode;
import com.cowlark.cowbel.ast.nodes.DummyExpressionNode;
import com.cowlark.cowbel.ast.nodes.ExpressionListNode;
import com.cowlark.cowbel.ast.nodes.ExpressionStatementNode;
import com.cowlark.cowbel.ast.nodes.ExternStatementNode;
import com.cowlark.cowbel.ast.nodes.FunctionDefinitionNode;
import com.cowlark.cowbel.ast.nodes.FunctionHeaderNode;
import com.cowlark.cowbel.ast.nodes.FunctionScopeConstructorNode;
import com.cowlark.cowbel.ast.nodes.GotoStatementNode;
import com.cowlark.cowbel.ast.nodes.IdentifierListNode;
import com.cowlark.cowbel.ast.nodes.IdentifierNode;
import com.cowlark.cowbel.ast.nodes.IfElseStatementNode;
import com.cowlark.cowbel.ast.nodes.IfStatementNode;
import com.cowlark.cowbel.ast.nodes.ImplementsStatementNode;
import com.cowlark.cowbel.ast.nodes.IndirectFunctionCallExpressionNode;
import com.cowlark.cowbel.ast.nodes.IndirectFunctionCallStatementNode;
import com.cowlark.cowbel.ast.nodes.InferredTypeNode;
import com.cowlark.cowbel.ast.nodes.IntegerConstantNode;
import com.cowlark.cowbel.ast.nodes.InterfaceTypeNode;
import com.cowlark.cowbel.ast.nodes.LabelStatementNode;
import com.cowlark.cowbel.ast.nodes.MethodCallExpressionNode;
import com.cowlark.cowbel.ast.nodes.MethodCallStatementNode;
import com.cowlark.cowbel.ast.nodes.Node;
import com.cowlark.cowbel.ast.nodes.ParameterDeclarationListNode;
import com.cowlark.cowbel.ast.nodes.ParameterDeclarationNode;
import com.cowlark.cowbel.ast.nodes.ReturnStatementNode;
import com.cowlark.cowbel.ast.nodes.ReturnVoidStatementNode;
import com.cowlark.cowbel.ast.nodes.StatementListNode;
import com.cowlark.cowbel.ast.nodes.StringConstantNode;
import com.cowlark.cowbel.ast.nodes.TypeAssignmentNode;
import com.cowlark.cowbel.ast.nodes.TypeListNode;
import com.cowlark.cowbel.ast.nodes.TypeVariableNode;
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
	
	public void visit(BlockScopeConstructorNode node) throws CompilationException
	{
		visit((Node) node);
	}
		
	public void visit(FunctionScopeConstructorNode node) throws CompilationException
	{
		visit((Node) node);
	}
		
	public void visit(FunctionDefinitionNode node) throws CompilationException
	{
		visit((Node) node);
	}

	public void visit(FunctionHeaderNode node) throws CompilationException
	{
		visit((Node) node);
	}

	public void visit(ParameterDeclarationListNode node) throws CompilationException
	{
		visit((Node) node);
	}

	public void visit(ParameterDeclarationNode node) throws CompilationException
	{
		visit((Node) node);
	}

	public void visit(TypeAssignmentNode node) throws CompilationException
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
	
	public void visit(DirectFunctionCallExpressionNode node) throws CompilationException
	{
		visit((Node) node);
	}
	
	public void visit(DirectFunctionCallStatementNode node) throws CompilationException
	{
		visit((Node) node);
	}
	
	public void visit(IndirectFunctionCallStatementNode node) throws CompilationException
	{
		visit((Node) node);
	}
	
	public void visit(IndirectFunctionCallExpressionNode node) throws CompilationException
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
	
	public void visit(IdentifierNode node) throws CompilationException
	{
		visit((Node) node);
	}

	public void visit(IdentifierListNode node) throws CompilationException
	{
		visit((Node) node);
	}

	public void visit(MethodCallExpressionNode node) throws CompilationException
	{
		visit((Node) node);
	}
	
	public void visit(MethodCallStatementNode node) throws CompilationException
	{
		visit((Node) node);
	}

	public void visit(TypeListNode node) throws CompilationException
	{
		visit((Node) node);
	}
	
	public void visit(TypeVariableNode node) throws CompilationException
	{
		visit((Node) node);
	}
	
	public void visit(InferredTypeNode node) throws CompilationException
	{
		visit((Node) node);
	}
	
	public void visit(ExpressionListNode node) throws CompilationException
	{
		visit((Node) node);
	}
	
	public void visit(BlockExpressionNode node) throws CompilationException
	{
		visit((Node) node);
	}	
	
	public void visit(InterfaceTypeNode node) throws CompilationException
	{
		visit((Node) node);
	}
	
	public void visit(ImplementsStatementNode node) throws CompilationException
	{
		visit((Node) node);
	}
	
	public void visit(ExternStatementNode node) throws CompilationException
	{
		visit((Node) node);
	}	
}
