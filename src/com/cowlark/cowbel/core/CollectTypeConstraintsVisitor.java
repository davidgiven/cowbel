/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING.BSD in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel.core;

import java.util.TreeSet;
import com.cowlark.cowbel.ast.BlockExpressionNode;
import com.cowlark.cowbel.ast.BlockScopeConstructorNode;
import com.cowlark.cowbel.ast.DirectFunctionCallExpressionNode;
import com.cowlark.cowbel.ast.DirectFunctionCallStatementNode;
import com.cowlark.cowbel.ast.DummyExpressionNode;
import com.cowlark.cowbel.ast.ExpressionListNode;
import com.cowlark.cowbel.ast.FunctionDefinitionNode;
import com.cowlark.cowbel.ast.FunctionHeaderNode;
import com.cowlark.cowbel.ast.FunctionScopeConstructorNode;
import com.cowlark.cowbel.ast.IdentifierListNode;
import com.cowlark.cowbel.ast.IndirectFunctionCallExpressionNode;
import com.cowlark.cowbel.ast.IntegerConstantNode;
import com.cowlark.cowbel.ast.MethodCallExpressionNode;
import com.cowlark.cowbel.ast.MethodCallStatementNode;
import com.cowlark.cowbel.ast.RealConstantNode;
import com.cowlark.cowbel.ast.RecursiveASTVisitor;
import com.cowlark.cowbel.ast.ReturnStatementNode;
import com.cowlark.cowbel.ast.StringConstantNode;
import com.cowlark.cowbel.ast.VarAssignmentNode;
import com.cowlark.cowbel.ast.VarReferenceNode;
import com.cowlark.cowbel.errors.CompilationException;
import com.cowlark.cowbel.errors.FunctionParameterMismatch;
import com.cowlark.cowbel.interfaces.IsNode;

public class CollectTypeConstraintsVisitor extends RecursiveASTVisitor
{
	public static CollectTypeConstraintsVisitor Instance =
		new CollectTypeConstraintsVisitor();
	
	private TreeSet<IsNode> _unhandledNodes = new TreeSet<IsNode>();
	private TypeRef _intTypeRef = Compiler.Instance.getCanonicalPrimitiveTypeRef("int");
	private TypeRef _realTypeRef = Compiler.Instance.getCanonicalPrimitiveTypeRef("real");
	private TypeRef _stringTypeRef = Compiler.Instance.getCanonicalPrimitiveTypeRef("string");
	
	private void defer_node(IsNode node)
	{
		_unhandledNodes.add(node);
	}
	
	public TreeSet<IsNode> getUnhandledNodes()
	{
		return _unhandledNodes;
	}
	
	public void unhandledNodeIsHandled(IsNode node)
	{
		_unhandledNodes.remove(node);
	}
	
	@Override
	public void visit(IntegerConstantNode node) throws CompilationException
	{
		node.getTypeRef().addParent(_intTypeRef);
	}
	
	@Override
	public void visit(RealConstantNode node) throws CompilationException
	{
		node.getTypeRef().addParent(_realTypeRef);
	}
	
	@Override
	public void visit(StringConstantNode node) throws CompilationException
	{
		node.getTypeRef().addParent(_stringTypeRef);
	}
	
	@Override
	public void visit(FunctionDefinitionNode node) throws CompilationException
	{
	}
	
	@Override
	public void visit(DummyExpressionNode node) throws CompilationException
	{
	    super.visit(node);
		node.getTypeRef().addParent(node.getTypeRef());
	}
	
	@Override
	public void visit(VarReferenceNode node) throws CompilationException
	{
		node.getTypeRef().addParent(node.getSymbol().getTypeRef());
	}

	@Override
    public void visit(IndirectFunctionCallExpressionNode node) throws CompilationException
	{
		assert(false);
		/*
		AbstractExpressionNode function = node.getFunction();
		FunctionType functionType = get_function_type(function);
		validate_function_call(node, function);
		*/
	}
	
	@Override
    public void visit(DirectFunctionCallExpressionNode node) throws CompilationException
	{
		Callable callable = node.getCallable();
		assert(callable != null);
		FunctionHeaderNode header = callable.getNode();
		
		if (!Utils.attachInputTypeConstraints(node, header.getInputParametersNode(), node) ||
			!Utils.attachSingleOutputTypeConstraint(node, header.getOutputParametersNode()))
		{
			throw new FunctionParameterMismatch(node, callable);
		}
		
		super.visit(node);
	}
	
	@Override
	public void visit(DirectFunctionCallStatementNode node)
	        throws CompilationException
	{
		Callable callable = node.getCallable();
		assert(callable != null);
		FunctionHeaderNode header = callable.getNode();
		
		if (!Utils.attachInputTypeConstraints(node, header.getInputParametersNode(), node) ||
			!Utils.attachOutputTypeConstraints(node, header.getOutputParametersNode(), node))
		{
			throw new FunctionParameterMismatch(node, callable);
		}
				
	    super.visit(node);
	}
	
	@Override
	public void visit(MethodCallExpressionNode node) throws CompilationException
	{
		defer_node(node);
		super.visit(node);
	}
	
	@Override
	public void visit(MethodCallStatementNode node) throws CompilationException
	{
		defer_node(node);
		super.visit(node);
	}
	
	@Override
	public void visit(BlockExpressionNode node) throws CompilationException
	{
		super.visit(node);
		
		BlockScopeConstructorNode block = node.getBlock();
		node.getTypeRef().setImplementation(block.getImplementation());
	}
	
	@Override
	public void visit(FunctionScopeConstructorNode node)
	        throws CompilationException
	{
	    super.visit(node);
	}
	
	@Override
	public void visit(VarAssignmentNode node) throws CompilationException
	{
		IdentifierListNode identifiers = node.getVariables();
		ExpressionListNode expressions = node.getExpressions();
		
		/* Add a constraint that the type of the expression must be
		 * downcastable to the type of the symbol. */
		
		for (int i = 0; i < identifiers.getNumberOfChildren(); i++)
		{
			identifiers.getSymbol(i).getTypeRef().addParent(
					expressions.getExpression(i).getTypeRef());
		}
		
	    super.visit(node);
	}
	
	@Override
	public void visit(ReturnStatementNode node) throws CompilationException
	{
	    super.visit(node);

	    TypeRef outputtype = node.getScope().getFunction().getOutputTypes().get(0);
		outputtype.addParent(node.getValue().getTypeRef());
	}
}
