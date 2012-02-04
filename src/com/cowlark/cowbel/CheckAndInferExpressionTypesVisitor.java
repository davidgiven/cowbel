/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import com.cowlark.cowbel.ast.HasInputs;
import com.cowlark.cowbel.ast.SimpleVisitor;
import com.cowlark.cowbel.ast.nodes.AbstractExpressionNode;
import com.cowlark.cowbel.ast.nodes.ArrayConstructorNode;
import com.cowlark.cowbel.ast.nodes.BooleanConstantNode;
import com.cowlark.cowbel.ast.nodes.DirectFunctionCallExpressionNode;
import com.cowlark.cowbel.ast.nodes.DummyExpressionNode;
import com.cowlark.cowbel.ast.nodes.ExpressionListNode;
import com.cowlark.cowbel.ast.nodes.FunctionDefinitionNode;
import com.cowlark.cowbel.ast.nodes.IdentifierNode;
import com.cowlark.cowbel.ast.nodes.IndirectFunctionCallExpressionNode;
import com.cowlark.cowbel.ast.nodes.IntegerConstantNode;
import com.cowlark.cowbel.ast.nodes.MethodCallExpressionNode;
import com.cowlark.cowbel.ast.nodes.Node;
import com.cowlark.cowbel.ast.nodes.StringConstantNode;
import com.cowlark.cowbel.ast.nodes.VarReferenceNode;
import com.cowlark.cowbel.errors.AttemptToCallNonFunctionTypeException;
import com.cowlark.cowbel.errors.CompilationException;
import com.cowlark.cowbel.errors.FunctionParameterMismatch;
import com.cowlark.cowbel.errors.InvalidFunctionCallInExpressionContext;
import com.cowlark.cowbel.methods.Method;
import com.cowlark.cowbel.symbols.Symbol;
import com.cowlark.cowbel.types.ArrayType;
import com.cowlark.cowbel.types.BooleanType;
import com.cowlark.cowbel.types.FunctionType;
import com.cowlark.cowbel.types.InferredType;
import com.cowlark.cowbel.types.IntegerType;
import com.cowlark.cowbel.types.StringType;
import com.cowlark.cowbel.types.Type;

public class CheckAndInferExpressionTypesVisitor extends SimpleVisitor
{
	@Override
	public void visit(FunctionDefinitionNode node) throws CompilationException
	{
	}
	
	@Override
	public void visit(DummyExpressionNode node) throws CompilationException
	{
		Type type = node.getChild().calculateType();
		node.setType(type);
	}
	
	@Override
	public void visit(BooleanConstantNode node) throws CompilationException
	{
		node.setType(BooleanType.create());
	}
	
	@Override
	public void visit(StringConstantNode node) throws CompilationException
	{
		node.setType(StringType.create());
	}
	
	@Override
	public void visit(IntegerConstantNode node) throws CompilationException
	{
		node.setType(IntegerType.create());
	}
	
	@Override
	public void visit(VarReferenceNode node) throws CompilationException
	{
		Symbol symbol = node.getSymbol();
		Type type = symbol.getSymbolType();
		type.ensureConcrete(node);
		node.setType(type);
	}

	private static FunctionType get_function_type(AbstractExpressionNode expression)
			throws CompilationException
	{
		Type type = expression.calculateType();
		if (type instanceof FunctionType)
			return (FunctionType) type;
		
		throw new AttemptToCallNonFunctionTypeException(expression);
	}
	
	private <T extends AbstractExpressionNode & HasInputs> void validate_function_call(
			T node, Function function) throws CompilationException
	{
		FunctionType functionType = function.getType();
		List<Type> inputArgumentTypes = functionType.getInputArgumentTypes();
		List<Type> outputArgumentTypes = functionType.getOutputArgumentTypes();
		ExpressionListNode callArguments = node.getInputs();
		
		Vector<Type> callArgumentTypes = new Vector<Type>();
		for (Node n : callArguments)
		{
			AbstractExpressionNode e = (AbstractExpressionNode) n;
			Type t = e.calculateType();
			callArgumentTypes.add(t);
		}
		
		if (!Utils.unifyTypeLists(node, inputArgumentTypes, callArgumentTypes, false))
			throw new FunctionParameterMismatch(node,function,
					outputArgumentTypes, null,
					inputArgumentTypes, callArgumentTypes);
		
		if (outputArgumentTypes.size() != 1)
			throw new InvalidFunctionCallInExpressionContext(node,
					inputArgumentTypes, outputArgumentTypes);
		
		node.setType(outputArgumentTypes.get(0));
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
		Function function = node.getFunction();
		assert(function != null);
		
		validate_function_call(node, function);
	}
	
	@Override
	public void visit(ArrayConstructorNode node) throws CompilationException
	{
		List<AbstractExpressionNode> members = node.getListMembers();
		Type type = InferredType.create();
		
		for (AbstractExpressionNode exp : members)
		{
			Type t = exp.calculateType();
			type.unifyWith(node, t);
		}
		
		node.setType(ArrayType.create(type));
	}
	
	@Override
	public void visit(MethodCallExpressionNode node) throws CompilationException
	{
		AbstractExpressionNode receiver = node.getMethodReceiver();
		Type receivertype = receiver.calculateType();
		receivertype.ensureConcrete(node);
		
		ExpressionListNode arguments = node.getInputs();
		ArrayList<Type> argumenttypes = new ArrayList<Type>();
		for (Node n : arguments)
		{
			AbstractExpressionNode e = (AbstractExpressionNode) n; 
			Type type = e.calculateType();
			argumenttypes.add(type);
		}
		
		IdentifierNode name = node.getMethodIdentifier();
		Method method = receivertype.lookupMethod(node, name);
		node.setMethod(method);
		method.typeCheck(node, null, argumenttypes);
		
		List<Type> outputArgumentTypes = method.getOutputTypes();
		if (outputArgumentTypes.size() != 1)
			throw new InvalidFunctionCallInExpressionContext(node,
					method.getInputTypes(), outputArgumentTypes);
		
		node.setType(outputArgumentTypes.get(0));
	}
	
	@Override
	public void visit(Node node) throws CompilationException
	{
		assert(false);
	}
}
