/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING.BSD in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel;

import java.util.List;
import com.cowlark.cowbel.ast.AbstractExpressionNode;
import com.cowlark.cowbel.ast.AbstractScopeConstructorNode;
import com.cowlark.cowbel.ast.AbstractStatementNode;
import com.cowlark.cowbel.ast.BlockScopeConstructorNode;
import com.cowlark.cowbel.ast.BreakStatementNode;
import com.cowlark.cowbel.ast.ContinueStatementNode;
import com.cowlark.cowbel.ast.DirectFunctionCallStatementNode;
import com.cowlark.cowbel.ast.DoWhileStatementNode;
import com.cowlark.cowbel.ast.ExpressionListNode;
import com.cowlark.cowbel.ast.ExpressionStatementNode;
import com.cowlark.cowbel.ast.ExternStatementNode;
import com.cowlark.cowbel.ast.FunctionDefinitionNode;
import com.cowlark.cowbel.ast.FunctionScopeConstructorNode;
import com.cowlark.cowbel.ast.GotoStatementNode;
import com.cowlark.cowbel.ast.IdentifierListNode;
import com.cowlark.cowbel.ast.IdentifierNode;
import com.cowlark.cowbel.ast.IfElseStatementNode;
import com.cowlark.cowbel.ast.IfStatementNode;
import com.cowlark.cowbel.ast.ImplementsStatementNode;
import com.cowlark.cowbel.ast.LabelStatementNode;
import com.cowlark.cowbel.ast.MethodCallStatementNode;
import com.cowlark.cowbel.ast.Node;
import com.cowlark.cowbel.ast.ReturnStatementNode;
import com.cowlark.cowbel.ast.ReturnVoidStatementNode;
import com.cowlark.cowbel.ast.SimpleASTVisitor;
import com.cowlark.cowbel.ast.TypeAssignmentNode;
import com.cowlark.cowbel.ast.VarAssignmentNode;
import com.cowlark.cowbel.ast.VarDeclarationNode;
import com.cowlark.cowbel.ast.VarReferenceNode;
import com.cowlark.cowbel.ast.WhileStatementNode;
import com.cowlark.cowbel.errors.CompilationException;
import com.cowlark.cowbel.errors.FunctionParameterMismatch;
import com.cowlark.cowbel.errors.InvalidExpressionReturn;
import com.cowlark.cowbel.errors.MethodParameterMismatch;
import com.cowlark.cowbel.interfaces.HasInputs;
import com.cowlark.cowbel.interfaces.HasOutputs;
import com.cowlark.cowbel.methods.Method;
import com.cowlark.cowbel.symbols.Symbol;
import com.cowlark.cowbel.types.BooleanType;
import com.cowlark.cowbel.types.FunctionType;
import com.cowlark.cowbel.types.Type;

public class CheckAndInferStatementTypesVisitor extends SimpleASTVisitor
{
	@Override
	public void visit(FunctionDefinitionNode node) throws CompilationException
	{
	}
	
	private void visit(AbstractScopeConstructorNode node) throws CompilationException
	{
		node.getChild().visit(this);
	}
	
	@Override
	public void visit(FunctionScopeConstructorNode node) throws CompilationException
	{
		visit((AbstractScopeConstructorNode) node);
	}
	
	@Override
	public void visit(BlockScopeConstructorNode node) throws CompilationException
	{
		visit((AbstractScopeConstructorNode) node);
	}
	
	@Override
	public void visit(TypeAssignmentNode node) throws CompilationException
	{
	}
	
	@Override
	public void visit(VarDeclarationNode node) throws CompilationException
	{
	}
	
	@Override
	public void visit(VarAssignmentNode node) throws CompilationException
	{
		IdentifierListNode ids = node.getVariables();
		ExpressionListNode exprs = node.getExpressions();
		assert(ids.getNumberOfChildren() == exprs.getNumberOfChildren());
		
		for (int i = 0; i < ids.getNumberOfChildren(); i++)
		{
			Symbol symbol = ids.getSymbol(i);
			Type symboltype = symbol.getType();
			
			AbstractExpressionNode expression = exprs.getExpression(i);
			Type expressiontype = expression.calculateType();

			symboltype.unifyWith(node, expressiontype);
			symboltype.ensureConcrete(node);
		}
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
		AbstractExpressionNode value = node.getValue();
		Type valuetype = value.calculateType();
		
		Function function = node.getScope().getFunctionScope().getFunction();
		FunctionType functiontype = function.getType();
		List<Type> returntypes = functiontype.getOutputArgumentTypes();
		
		if (returntypes.size() != 1)
			throw new InvalidExpressionReturn(node, function);
		
		Type returntype = returntypes.get(0);
		valuetype.ensureConcrete(node);
		returntype.unifyWith(node, valuetype);
	}

	@Override
	public void visit(ReturnVoidStatementNode node) throws CompilationException
	{
	}

	@Override
	public void visit(IfStatementNode node) throws CompilationException
	{
		AbstractExpressionNode conditional = node.getConditionalExpression();
		Type conditionaltype = conditional.calculateType();
		conditionaltype.unifyWith(node, BooleanType.create());
		
		AbstractStatementNode positive = node.getPositiveStatement();
		positive.checkTypes();
	}

	@Override
	public void visit(IfElseStatementNode node) throws CompilationException
	{
		AbstractExpressionNode conditional = node.getConditionalExpression();
		Type conditionaltype = conditional.calculateType();
		conditionaltype.unifyWith(node, BooleanType.create());
		
		AbstractStatementNode positive = node.getPositiveStatement();
		positive.checkTypes();
		
		AbstractStatementNode negative = node.getNegativeStatement();
		negative.checkTypes();
	}

	@Override
	public void visit(WhileStatementNode node) throws CompilationException
	{
		AbstractExpressionNode conditional = node.getConditionalExpression();
		Type conditionaltype = conditional.calculateType();
		conditionaltype.unifyWith(node, BooleanType.create());
		
		AbstractStatementNode body = node.getBodyStatement();
		body.checkTypes();
	}

	@Override
	public void visit(DoWhileStatementNode node) throws CompilationException
	{
		AbstractExpressionNode conditional = node.getConditionalExpression();
		Type conditionaltype = conditional.calculateType();
		conditionaltype.unifyWith(node, BooleanType.create());
		
		AbstractStatementNode body = node.getBodyStatement();
		body.checkTypes();
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
	
	private <T extends AbstractStatementNode & HasInputs & HasOutputs>
	void validate_method_call(T node, Method method)
		throws CompilationException
	{
		List<Type> inputFunctionTypes = method.getInputTypes();
		List<Type> outputFunctionTypes = method.getOutputTypes();
		
		List<Type> inputCallTypes = node.getInputs().calculateTypes();
		List<Type> outputCallTypes = node.getOutputs().calculateTypes();
		
		if (!Utils.unifyTypeLists(node, inputFunctionTypes, inputCallTypes, false) ||
			!Utils.unifyTypeLists(node, outputFunctionTypes, outputCallTypes, true))
		{
			throw new MethodParameterMismatch(node, method,
					outputFunctionTypes, outputCallTypes,
					inputFunctionTypes, inputCallTypes);
		}
	}

	@Override
	public void visit(MethodCallStatementNode node) throws CompilationException
	{
		AbstractExpressionNode receiver = node.getMethodReceiver();
		Type receivertype = receiver.calculateType();
		receivertype.ensureConcrete(node);
		
		IdentifierNode name = node.getMethodIdentifier();
		Method method = receivertype.lookupMethod(node, name);
		node.setMethod(method);
		
		validate_method_call(node, method);
	}
	
	private <T extends AbstractStatementNode & HasInputs & HasOutputs>
		void validate_function_call(T node, Function function)
			throws CompilationException
	{
		FunctionType functionType = (FunctionType) function.getType();
		
		List<Type> inputFunctionTypes = functionType.getInputArgumentTypes();
		List<Type> outputFunctionTypes = functionType.getOutputArgumentTypes();
		
		List<Type> inputCallTypes = node.getInputs().calculateTypes();
		List<Type> outputCallTypes = node.getOutputs().calculateTypes();
		
		if (!Utils.unifyTypeLists(node, inputFunctionTypes, inputCallTypes, false) ||
			!Utils.unifyTypeLists(node, outputFunctionTypes, outputCallTypes, true))
		{
			throw new FunctionParameterMismatch(node, function,
					outputFunctionTypes, outputCallTypes,
					inputFunctionTypes, inputCallTypes);
		}
	}
	
	@Override
    public void visit(DirectFunctionCallStatementNode node) throws CompilationException
	{
		Function function = node.getFunction();
		assert(function != null);
		
		validate_function_call(node, function);
	}
	
	@Override
	public void visit(ImplementsStatementNode node) throws CompilationException
	{
	}

	@Override
	public void visit(ExternStatementNode node) throws CompilationException
	{
		for (Node n : node)
		{
			VarReferenceNode varnode = (VarReferenceNode) n;
			varnode.calculateType();
		}
	}
	
	@Override
	public void visit(Node node) throws CompilationException
	{
		assert(false);
	}
}
