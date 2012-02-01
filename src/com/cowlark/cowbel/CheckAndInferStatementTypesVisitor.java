/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel;

import java.util.List;
import com.cowlark.cowbel.ast.HasInputs;
import com.cowlark.cowbel.ast.HasOutputs;
import com.cowlark.cowbel.ast.SimpleVisitor;
import com.cowlark.cowbel.ast.nodes.BreakStatementNode;
import com.cowlark.cowbel.ast.nodes.ContinueStatementNode;
import com.cowlark.cowbel.ast.nodes.DirectFunctionCallStatementNode;
import com.cowlark.cowbel.ast.nodes.DoWhileStatementNode;
import com.cowlark.cowbel.ast.nodes.ExpressionListNode;
import com.cowlark.cowbel.ast.nodes.ExpressionNode;
import com.cowlark.cowbel.ast.nodes.ExpressionStatementNode;
import com.cowlark.cowbel.ast.nodes.FunctionDefinitionNode;
import com.cowlark.cowbel.ast.nodes.GotoStatementNode;
import com.cowlark.cowbel.ast.nodes.IdentifierListNode;
import com.cowlark.cowbel.ast.nodes.IdentifierNode;
import com.cowlark.cowbel.ast.nodes.IfElseStatementNode;
import com.cowlark.cowbel.ast.nodes.IfStatementNode;
import com.cowlark.cowbel.ast.nodes.LabelStatementNode;
import com.cowlark.cowbel.ast.nodes.MethodCallStatementNode;
import com.cowlark.cowbel.ast.nodes.Node;
import com.cowlark.cowbel.ast.nodes.ReturnStatementNode;
import com.cowlark.cowbel.ast.nodes.ReturnVoidStatementNode;
import com.cowlark.cowbel.ast.nodes.ScopeConstructorNode;
import com.cowlark.cowbel.ast.nodes.StatementNode;
import com.cowlark.cowbel.ast.nodes.VarAssignmentNode;
import com.cowlark.cowbel.ast.nodes.VarDeclarationNode;
import com.cowlark.cowbel.ast.nodes.WhileStatementNode;
import com.cowlark.cowbel.errors.CompilationException;
import com.cowlark.cowbel.errors.FunctionParameterMismatch;
import com.cowlark.cowbel.errors.InvalidExpressionReturn;
import com.cowlark.cowbel.methods.Method;
import com.cowlark.cowbel.symbols.Function;
import com.cowlark.cowbel.symbols.Symbol;
import com.cowlark.cowbel.types.BooleanType;
import com.cowlark.cowbel.types.FunctionType;
import com.cowlark.cowbel.types.Type;

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
			Type symboltype = symbol.getSymbolType();
			
			ExpressionNode expression = exprs.getExpression(i);
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
		ExpressionNode value = node.getValue();
		Type valuetype = value.calculateType();
		
		Function function = node.getScope().getFunctionScope().getFunction();
		FunctionType functiontype = (FunctionType) function.getSymbolType();
		List<Type> returntypes = functiontype.getOutputArgumentTypes();
		
		if (returntypes.size() != 1)
			throw new InvalidExpressionReturn(node, function);
		
		Type returntype = returntypes.get(0);
		valuetype.unifyWith(node, returntype);
		valuetype.ensureConcrete(node);
	}

	@Override
	public void visit(ReturnVoidStatementNode node) throws CompilationException
	{
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
	public void visit(MethodCallStatementNode node) throws CompilationException
	{
		ExpressionNode receiver = node.getMethodReceiver();
		Type receivertype = receiver.calculateType();
		receivertype.ensureConcrete(node);
		
		IdentifierListNode variables = node.getOutputs();
		List<Type> variabletypes = variables.calculateTypes();
		
		ExpressionListNode arguments = node.getInputs();
		List<Type> argumenttypes = arguments.calculateTypes();
		
		IdentifierNode name = node.getMethodIdentifier();
		Method method = receivertype.lookupMethod(node, name);
		node.setMethod(method);
		method.typeCheck(node, variabletypes, argumenttypes);
	}
	
	private <T extends StatementNode & HasInputs & HasOutputs>
		void validate_function_call(T node, Function function)
			throws CompilationException
	{
		FunctionType functionType = (FunctionType) function.getSymbolType();
		
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
		Symbol symbol = node.getSymbol();
		assert(symbol instanceof Function);
		Function function = (Function) symbol;
		
		validate_function_call(node, function);
	}
	

	@Override
	public void visit(Node node) throws CompilationException
	{
		assert(false);
	}
}
