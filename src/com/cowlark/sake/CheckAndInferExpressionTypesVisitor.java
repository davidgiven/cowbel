package com.cowlark.sake;

import java.util.List;
import java.util.Vector;
import com.cowlark.sake.ast.SimpleVisitor;
import com.cowlark.sake.ast.nodes.ExpressionNode;
import com.cowlark.sake.ast.nodes.FunctionCallNode;
import com.cowlark.sake.ast.nodes.Node;
import com.cowlark.sake.ast.nodes.StringConstantNode;
import com.cowlark.sake.ast.nodes.VarReferenceNode;
import com.cowlark.sake.errors.AttemptToCallNonFunctionTypeException;
import com.cowlark.sake.errors.CompilationException;
import com.cowlark.sake.errors.FunctionParameterMismatch;
import com.cowlark.sake.types.FunctionType;
import com.cowlark.sake.types.StringType;
import com.cowlark.sake.types.Type;

public class CheckAndInferExpressionTypesVisitor extends SimpleVisitor
{
	@Override
	public void visit(StringConstantNode node) throws CompilationException
	{
		node.setType(StringType.create());
	}
	
	@Override
	public void visit(VarReferenceNode node) throws CompilationException
	{
		Symbol symbol = node.getSymbol();
		Type type = symbol.getSymbolType();
		type.checkType(node);
		node.setType(type);
	}

	private static FunctionType get_function_type(ExpressionNode expression)
			throws CompilationException
	{
		Type type = expression.calculateType();
		if (type instanceof FunctionType)
			return (FunctionType) type;
		
		throw new AttemptToCallNonFunctionTypeException(expression);
	}
	
	public void visit(FunctionCallNode node) throws CompilationException
	{
		ExpressionNode function = node.getFunction();
		FunctionType functionType = get_function_type(function);
		List<Type> functionArgumentTypes = functionType.getArgumentTypes();
		List<ExpressionNode> callArguments = node.getArguments();
		
		Vector<Type> callArgumentTypes = new Vector<Type>();
		for (ExpressionNode n : callArguments)
		{
			Type t = n.calculateType();
			callArgumentTypes.add(t);
		}
		
		if (!functionArgumentTypes.equals(callArgumentTypes))
			throw new FunctionParameterMismatch(node, functionArgumentTypes,
					callArgumentTypes);
		
		node.setType(functionType.getReturnType());
	}
	
	@Override
	public void visit(Node node) throws CompilationException
	{
		assert(false);
	}
}
