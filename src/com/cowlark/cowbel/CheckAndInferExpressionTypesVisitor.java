/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING.BSD in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import com.cowlark.cowbel.ast.AbstractExpressionNode;
import com.cowlark.cowbel.ast.BlockExpressionNode;
import com.cowlark.cowbel.ast.BlockScopeConstructorNode;
import com.cowlark.cowbel.ast.BooleanConstantNode;
import com.cowlark.cowbel.ast.DirectFunctionCallExpressionNode;
import com.cowlark.cowbel.ast.DummyExpressionNode;
import com.cowlark.cowbel.ast.ExpressionListNode;
import com.cowlark.cowbel.ast.FunctionDefinitionNode;
import com.cowlark.cowbel.ast.IdentifierNode;
import com.cowlark.cowbel.ast.IndirectFunctionCallExpressionNode;
import com.cowlark.cowbel.ast.IntegerConstantNode;
import com.cowlark.cowbel.ast.MethodCallExpressionNode;
import com.cowlark.cowbel.ast.Node;
import com.cowlark.cowbel.ast.RealConstantNode;
import com.cowlark.cowbel.ast.SimpleASTVisitor;
import com.cowlark.cowbel.ast.StringConstantNode;
import com.cowlark.cowbel.ast.VarReferenceNode;
import com.cowlark.cowbel.errors.AttemptToCallNonFunctionTypeException;
import com.cowlark.cowbel.errors.CompilationException;
import com.cowlark.cowbel.errors.FunctionParameterMismatch;
import com.cowlark.cowbel.errors.InvalidFunctionCallInExpressionContext;
import com.cowlark.cowbel.interfaces.HasInputs;
import com.cowlark.cowbel.methods.Method;
import com.cowlark.cowbel.symbols.Symbol;
import com.cowlark.cowbel.types.BooleanType;
import com.cowlark.cowbel.types.ClassType;
import com.cowlark.cowbel.types.FunctionType;
import com.cowlark.cowbel.types.IntegerType;
import com.cowlark.cowbel.types.RealType;
import com.cowlark.cowbel.types.StringType;
import com.cowlark.cowbel.types.Type;

public class CheckAndInferExpressionTypesVisitor extends SimpleASTVisitor
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
	public void visit(RealConstantNode node) throws CompilationException
	{
		node.setType(RealType.create());
	}
	
	@Override
	public void visit(VarReferenceNode node) throws CompilationException
	{
		Symbol symbol = node.getSymbol();
		Type type = symbol.getType();
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
	public void visit(MethodCallExpressionNode node) throws CompilationException
	{
		AbstractExpressionNode receiver = node.getReceiver();
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
		
		IdentifierNode name = node.getIdentifier();
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
	public void visit(BlockExpressionNode node) throws CompilationException
	{
		BlockScopeConstructorNode block = node.getBlock();
		block.checkTypes();
		
		Type type = ClassType.create(block);
		node.setType(type);
	}
	
	@Override
	public void visit(Node node) throws CompilationException
	{
		assert(false);
	}
}
