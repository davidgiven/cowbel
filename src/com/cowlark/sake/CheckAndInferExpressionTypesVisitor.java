package com.cowlark.sake;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import com.cowlark.sake.ast.SimpleVisitor;
import com.cowlark.sake.ast.nodes.DummyExpressionNode;
import com.cowlark.sake.ast.nodes.ExpressionNode;
import com.cowlark.sake.ast.nodes.FunctionCallNode;
import com.cowlark.sake.ast.nodes.IdentifierNode;
import com.cowlark.sake.ast.nodes.ListConstructorNode;
import com.cowlark.sake.ast.nodes.MethodCallNode;
import com.cowlark.sake.ast.nodes.Node;
import com.cowlark.sake.ast.nodes.ReturnStatementNode;
import com.cowlark.sake.ast.nodes.StringConstantNode;
import com.cowlark.sake.ast.nodes.VarReferenceNode;
import com.cowlark.sake.errors.AttemptToCallNonFunctionTypeException;
import com.cowlark.sake.errors.CompilationException;
import com.cowlark.sake.errors.FunctionParameterMismatch;
import com.cowlark.sake.errors.TypesNotCompatibleException;
import com.cowlark.sake.methods.Method;
import com.cowlark.sake.types.FunctionType;
import com.cowlark.sake.types.ListType;
import com.cowlark.sake.types.StringType;
import com.cowlark.sake.types.Type;
import com.cowlark.sake.types.TypeVariable;

public class CheckAndInferExpressionTypesVisitor extends SimpleVisitor
{
	@Override
	public void visit(DummyExpressionNode node) throws CompilationException
	{
		Type type = node.getChild().calculateType();
		node.setType(type);
	}
	
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
		type.ensureConcrete(node);
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
	
	private static boolean compareArgumentTypes(FunctionCallNode node,
			List<Type> funclist, List<Type> calllist) throws CompilationException
	{
		if (funclist.size() != calllist.size())
			return false;
		
		try
		{
			for (int i = 0; i < funclist.size(); i++)
			{
				Type t1 = funclist.get(i);
				Type t2 = calllist.get(i);
				t1.unifyWith(node, t2);
				t2.ensureConcrete(node);
			}
		}
		catch (TypesNotCompatibleException e)
		{
			return false;
		}
		
		return true;	
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
		
		if (!compareArgumentTypes(node, functionArgumentTypes, callArgumentTypes))
			throw new FunctionParameterMismatch(node, functionArgumentTypes,
					callArgumentTypes);
		
		node.setType(functionType.getReturnType());
	}
	
	@Override
	public void visit(ListConstructorNode node) throws CompilationException
	{
		List<ExpressionNode> members = node.getListMembers();
		Type type = TypeVariable.create();
		
		for (ExpressionNode exp : members)
		{
			Type t = exp.calculateType();
			type.unifyWith(node, t);
		}
		
		node.setType(ListType.create(type));
	}
	
	@Override
	public void visit(MethodCallNode node) throws CompilationException
	{
		ExpressionNode receiver = node.getMethodReceiver();
		Type receivertype = receiver.calculateType();
		receivertype.ensureConcrete(node);
		
		List<ExpressionNode> arguments = node.getMethodArguments();
		ArrayList<Type> argumenttypes = new ArrayList<Type>();
		for (ExpressionNode n : arguments)
		{
			Type type = n.calculateType();
			argumenttypes.add(type);
		}
		
		IdentifierNode name = node.getMethodIdentifier();
		Method method = receivertype.lookupMethod(node, name);
		method.typeCheck(node, argumenttypes);
		node.setType(method.getReturnType());
	}
	
	@Override
	public void visit(Node node) throws CompilationException
	{
		assert(false);
	}
}
