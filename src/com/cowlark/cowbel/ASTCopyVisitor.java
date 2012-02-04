/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING.BSD in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel;

import com.cowlark.cowbel.ast.RecursiveVisitor;
import com.cowlark.cowbel.ast.nodes.ArrayConstructorNode;
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
import com.cowlark.cowbel.ast.nodes.FunctionDefinitionNode;
import com.cowlark.cowbel.ast.nodes.FunctionHeaderNode;
import com.cowlark.cowbel.ast.nodes.FunctionScopeConstructorNode;
import com.cowlark.cowbel.ast.nodes.GotoStatementNode;
import com.cowlark.cowbel.ast.nodes.IdentifierListNode;
import com.cowlark.cowbel.ast.nodes.IdentifierNode;
import com.cowlark.cowbel.ast.nodes.IfElseStatementNode;
import com.cowlark.cowbel.ast.nodes.IfStatementNode;
import com.cowlark.cowbel.ast.nodes.IndirectFunctionCallExpressionNode;
import com.cowlark.cowbel.ast.nodes.InferredTypeNode;
import com.cowlark.cowbel.ast.nodes.IntegerConstantNode;
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

public class ASTCopyVisitor extends RecursiveVisitor
{
	private Node _result = null;
	
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
    public void visit(ArrayConstructorNode node) throws CompilationException
	{
		_result = new ArrayConstructorNode(node.start(), node.end());
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
	public void visit(TypeListNode node) throws CompilationException
	{
		_result = new TypeListNode(node.start(), node.end());
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
	public void visit(Node node) throws CompilationException
	{
		Node result = _result;
		assert(result != null);
		
		for (Node n : node)
		{
			_result = null;
			n.visit(this);
			assert(_result != null);
			
			result.addChild(_result);
		}
		
		_result = result;
	}
}
