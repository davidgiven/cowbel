/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING.BSD in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel.ast;

import com.cowlark.cowbel.errors.CompilationException;

public class ASTVisitor
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
	
	public void visit(RealConstantNode node) throws CompilationException
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

	public void visit(InterfaceListNode node) throws CompilationException
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
	
	public void visit(TypeExternNode node) throws CompilationException
	{
		visit((Node) node);
	}
	
	public void visit(ExternExpressionNode node) throws CompilationException
	{
		visit((Node) node);
	}
	
}
