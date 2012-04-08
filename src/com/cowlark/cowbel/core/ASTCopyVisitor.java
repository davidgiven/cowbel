/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING.BSD in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel.core;

import com.cowlark.cowbel.ast.BlockExpressionNode;
import com.cowlark.cowbel.ast.BlockScopeConstructorNode;
import com.cowlark.cowbel.ast.BooleanConstantNode;
import com.cowlark.cowbel.ast.BreakStatementNode;
import com.cowlark.cowbel.ast.ContinueStatementNode;
import com.cowlark.cowbel.ast.DirectFunctionCallExpressionNode;
import com.cowlark.cowbel.ast.DirectFunctionCallStatementNode;
import com.cowlark.cowbel.ast.DoWhileStatementNode;
import com.cowlark.cowbel.ast.DummyExpressionNode;
import com.cowlark.cowbel.ast.ExpressionListNode;
import com.cowlark.cowbel.ast.ExpressionStatementNode;
import com.cowlark.cowbel.ast.ExternExpressionNode;
import com.cowlark.cowbel.ast.ExternStatementNode;
import com.cowlark.cowbel.ast.FunctionDefinitionNode;
import com.cowlark.cowbel.ast.FunctionHeaderNode;
import com.cowlark.cowbel.ast.FunctionScopeConstructorNode;
import com.cowlark.cowbel.ast.GotoStatementNode;
import com.cowlark.cowbel.ast.IdentifierListNode;
import com.cowlark.cowbel.ast.IdentifierNode;
import com.cowlark.cowbel.ast.IfElseStatementNode;
import com.cowlark.cowbel.ast.IfStatementNode;
import com.cowlark.cowbel.ast.ImplementsStatementNode;
import com.cowlark.cowbel.ast.IndirectFunctionCallExpressionNode;
import com.cowlark.cowbel.ast.IndirectFunctionCallStatementNode;
import com.cowlark.cowbel.ast.InferredTypeNode;
import com.cowlark.cowbel.ast.IntegerConstantNode;
import com.cowlark.cowbel.ast.InterfaceListNode;
import com.cowlark.cowbel.ast.InterfaceTypeNode;
import com.cowlark.cowbel.ast.LabelStatementNode;
import com.cowlark.cowbel.ast.MethodCallExpressionNode;
import com.cowlark.cowbel.ast.MethodCallStatementNode;
import com.cowlark.cowbel.ast.Node;
import com.cowlark.cowbel.ast.ParameterDeclarationListNode;
import com.cowlark.cowbel.ast.ParameterDeclarationNode;
import com.cowlark.cowbel.ast.RealConstantNode;
import com.cowlark.cowbel.ast.RecursiveASTVisitor;
import com.cowlark.cowbel.ast.ReturnStatementNode;
import com.cowlark.cowbel.ast.ReturnVoidStatementNode;
import com.cowlark.cowbel.ast.StatementListNode;
import com.cowlark.cowbel.ast.StringConstantNode;
import com.cowlark.cowbel.ast.TypeAssignmentNode;
import com.cowlark.cowbel.ast.TypeExternNode;
import com.cowlark.cowbel.ast.TypeVariableNode;
import com.cowlark.cowbel.ast.VarAssignmentNode;
import com.cowlark.cowbel.ast.VarDeclarationNode;
import com.cowlark.cowbel.ast.VarReferenceNode;
import com.cowlark.cowbel.ast.WhileStatementNode;
import com.cowlark.cowbel.errors.CompilationException;
import com.cowlark.cowbel.interfaces.IsNode;

public class ASTCopyVisitor extends RecursiveASTVisitor
{
	public static ASTCopyVisitor Instance =
		new ASTCopyVisitor();
	
	private Node _result = null;
	
	public void reset()
	{
		_result = null;
	}
	
	public Node getResult()
    {
	    return _result;
    }
	
	@Override
    public void visit(StatementListNode node) throws CompilationException
	{
		_result = new StatementListNode(node.start(), node.end());
		super.visit(node);
	}
	
	@Override
    public void visit(BlockScopeConstructorNode node) throws CompilationException
	{
		_result = new BlockScopeConstructorNode(node.start(), node.end());
		super.visit(node);
	}
	
	@Override
	public void visit(FunctionScopeConstructorNode node)
	        throws CompilationException
	{
	    _result = new FunctionScopeConstructorNode(node.start(), node.end());
	    super.visit(node);
	}
	
	@Override
    public void visit(FunctionDefinitionNode node) throws CompilationException
	{
		_result = new FunctionDefinitionNode(node.start(), node.end());
		super.visit(node);
	}

	@Override
	public void visit(FunctionHeaderNode node) throws CompilationException
	{
		_result = new FunctionHeaderNode(node.start(), node.end());
		super.visit(node);
	}

	@Override
	public void visit(ParameterDeclarationListNode node)
	        throws CompilationException
	{
	    _result = new ParameterDeclarationListNode(node.start(), node.end());
	    super.visit(node);
	}
	
	@Override
	public void visit(ParameterDeclarationNode node)
	        throws CompilationException
	{
	    _result = new ParameterDeclarationNode(node.start(), node.end());
	    super.visit(node);
	}
	
	@Override
	public void visit(TypeAssignmentNode node) throws CompilationException
	{
	    _result = new TypeAssignmentNode(node.start(), node.end());
	    super.visit(node);
	}
	
	@Override
    public void visit(VarDeclarationNode node) throws CompilationException
	{
		_result = new VarDeclarationNode(node.start(), node.end());
		super.visit(node);
	}

	@Override
    public void visit(VarAssignmentNode node) throws CompilationException
	{
		_result = new VarAssignmentNode(node.start(), node.end());
		super.visit(node);
	}

	@Override
    public void visit(ReturnStatementNode node) throws CompilationException
	{
		_result = new ReturnStatementNode(node.start(), node.end());
		super.visit(node);
	}
	
	@Override
    public void visit(ReturnVoidStatementNode node) throws CompilationException
	{
		_result = new ReturnVoidStatementNode(node.start(), node.end());
		super.visit(node);
	}

	@Override
    public void visit(BreakStatementNode node) throws CompilationException
	{
		_result = new BreakStatementNode(node.start(), node.end());
		super.visit(node);
	}

	@Override
    public void visit(ContinueStatementNode node) throws CompilationException
	{
		_result = new ContinueStatementNode(node.start(), node.end());
		super.visit(node);
	}

	@Override
    public void visit(IfStatementNode node) throws CompilationException
	{
		_result = new IfStatementNode(node.start(), node.end());
		super.visit(node);
	}

	@Override
    public void visit(IfElseStatementNode node) throws CompilationException
	{
		_result = new IfElseStatementNode(node.start(), node.end());
		super.visit(node);
	}

	@Override
    public void visit(WhileStatementNode node) throws CompilationException
	{
		_result = new WhileStatementNode(node.start(), node.end());
		super.visit(node);
	}

	@Override
    public void visit(DoWhileStatementNode node) throws CompilationException
	{
		_result = new DoWhileStatementNode(node.start(), node.end());
		super.visit(node);
	}

	@Override
    public void visit(LabelStatementNode node) throws CompilationException
	{
		_result = new LabelStatementNode(node.start(), node.end());
		super.visit(node);
	}

	@Override
    public void visit(GotoStatementNode node) throws CompilationException
	{
		_result = new GotoStatementNode(node.start(), node.end());
		super.visit(node);
	}

	@Override
    public void visit(ExpressionStatementNode node) throws CompilationException
	{
		_result = new ExpressionStatementNode(node.start(), node.end());
		super.visit(node);
	}

	@Override
    public void visit(DummyExpressionNode node) throws CompilationException
	{
		_result = new DummyExpressionNode(node.start(), node.end());
		super.visit(node);
	}
	
	@Override
    public void visit(VarReferenceNode node) throws CompilationException
	{
		_result = new VarReferenceNode(node.start(), node.end());
		super.visit(node);
	}
	
	@Override
    public void visit(DirectFunctionCallExpressionNode node) throws CompilationException
	{
		_result = new DirectFunctionCallExpressionNode(node.start(), node.end());
		super.visit(node);
	}
	
	@Override
    public void visit(DirectFunctionCallStatementNode node) throws CompilationException
	{
		_result = new DirectFunctionCallStatementNode(node.start(), node.end());
		super.visit(node);
	}
	
	@Override
    public void visit(IndirectFunctionCallStatementNode node) throws CompilationException
	{
		_result = new IndirectFunctionCallStatementNode(node.start(), node.end());
		super.visit(node);
	}
		
	@Override
    public void visit(IndirectFunctionCallExpressionNode node) throws CompilationException
	{
		_result = new IndirectFunctionCallExpressionNode(node.start(), node.end());
		super.visit(node);
	}
	
	@Override
    public void visit(BooleanConstantNode node) throws CompilationException
	{
		_result = new BooleanConstantNode(node.start(), node.end(), node.getValue());
		super.visit(node);
	}
	
	@Override
    public void visit(IntegerConstantNode node) throws CompilationException
	{
		_result = new IntegerConstantNode(node.start(), node.end(), node.getValue());
		super.visit(node);
	}
	
	@Override
    public void visit(RealConstantNode node) throws CompilationException
	{
		_result = new RealConstantNode(node.start(), node.end(), node.getValue());
		super.visit(node);
	}
	
	@Override
    public void visit(StringConstantNode node) throws CompilationException
	{
		_result = new StringConstantNode(node.start(), node.end(), node.getValue());
		super.visit(node);
	}
	
	@Override
	public void visit(IdentifierNode node) throws CompilationException
	{
		_result = new IdentifierNode(node.start(), node.end());
	    super.visit(node);
	}
	
	@Override
	public void visit(IdentifierListNode node) throws CompilationException
	{
		_result = new IdentifierListNode(node.start(), node.end());
	    super.visit(node);
	}
	
	@Override
    public void visit(MethodCallExpressionNode node) throws CompilationException
	{
		_result = new MethodCallExpressionNode(node.start(), node.end());
		super.visit(node);
	}
	
	@Override
    public void visit(MethodCallStatementNode node) throws CompilationException
	{
		_result = new MethodCallStatementNode(node.start(), node.end());
		super.visit(node);
	}

	@Override
	public void visit(InterfaceListNode node) throws CompilationException
	{
		_result = new InterfaceListNode(node.start(), node.end());
	    super.visit(node);
	}
	
	@Override
	public void visit(TypeVariableNode node) throws CompilationException
	{
	    _result = new TypeVariableNode(node.start(), node.end());
	    super.visit(node);
	}
	
	@Override
	public void visit(InferredTypeNode node) throws CompilationException
	{
	    _result = new InferredTypeNode(node.start(), node.end());
	    super.visit(node);
	}
	
	@Override
	public void visit(ExpressionListNode node) throws CompilationException
	{
	    _result = new ExpressionListNode(node.start(), node.end());
	    super.visit(node);
	}
	
	@Override
	public void visit(BlockExpressionNode node) throws CompilationException
	{
	    _result = new BlockExpressionNode(node.start(), node.end());
	    super.visit(node);
	}

	@Override
	public void visit(InterfaceTypeNode node) throws CompilationException
	{
	    _result = new InterfaceTypeNode(node.start(), node.end(),
	    		node.isExtern());
	    super.visit(node);
	}
	
	@Override
    public void visit(ImplementsStatementNode node) throws CompilationException
	{
		_result = new ImplementsStatementNode(node.start(), node.end(),
				node.isExtern());
		super.visit(node);
	};
	
	@Override
	public void visit(ExternStatementNode node) throws CompilationException
	{
	    _result = new ExternStatementNode(node.start(), node.end(),
	    		node.getTemplate());
	    super.visit(node);
	}
	
	@Override
	public void visit(TypeExternNode node) throws CompilationException
	{
		_result = new TypeExternNode(node.start(), node.end());
	    super.visit(node);
	}

	@Override
	public void visit(ExternExpressionNode node) throws CompilationException
	{
	    _result = new ExternExpressionNode(node.start(), node.end());
	    super.visit(node);
	}
	
	@Override
	public void visit(Node node) throws CompilationException
	{
		Node result = _result;
		assert(result != null);
		
		for (IsNode n : node)
		{
			_result = null;
			n.visit(this);
			assert(_result != null);
			
			result.addChild(_result);
		}
		
		_result = result;
	}
}
