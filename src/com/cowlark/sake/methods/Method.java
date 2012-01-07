package com.cowlark.sake.methods;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import com.cowlark.sake.ast.nodes.MethodCallNode;
import com.cowlark.sake.errors.CompilationException;
import com.cowlark.sake.errors.MethodParameterMismatch;
import com.cowlark.sake.errors.TypesNotCompatibleException;
import com.cowlark.sake.types.Type;

public abstract class Method
{
	private static HashMap<String, Method> _primitiveMethods;
	
	static
	{
		_primitiveMethods = new HashMap<String, Method>();
		registerPrimitiveMethod(new StringEqualsMethod());
		registerPrimitiveMethod(new StringAddMethod());
		registerPrimitiveMethod(new StringReplaceMethod());
		registerPrimitiveMethod(new StringSizeMethod());
		registerPrimitiveMethod(new StringPrintMethod());
	}
	
	public static Method lookupPrimitiveMethod(String signature)
	{
		return _primitiveMethods.get(signature);
	}
	
	public static void registerPrimitiveMethod(Method method)
	{
		_primitiveMethods.put(method.getSignature(), method);
	}
	
	private String _signature;
	private String _identifier;
	private Type _returnType;
	private List<Type> _argumentTypes;
	
	public Method()
    {
    }

	protected void setSignature(String signature, String identifier)
	{
		_signature = signature;
		_identifier = identifier;
	}
	
	protected void setSignature(String signature)
	{
		setSignature(signature, signature);
	}
	
	public String getSignature()
	{
		return _signature;
	}
	
	public String getIdentifier()
	{
		return _identifier;
	}
	
	protected void setReturnType(Type type)
	{
		_returnType = type;
	}
	
	public Type getReturnType()
	{
		return _returnType;
	}
	
	protected void setArgumentTypes(Type... types)
	{
		_argumentTypes = new ArrayList<Type>();
		for (Type t : types)
			_argumentTypes.add(t);
	}

	public List<Type> getArgumentTypes()
	{
		return _argumentTypes;
	}
	
	private boolean typeCheckImpl(MethodCallNode node, List<Type> callertypes)
			throws CompilationException
	{
		if (callertypes.size() != _argumentTypes.size())
			return false;
		
		try
		{
			for (int i = 0; i < callertypes.size(); i++)
			{
				Type methodtype = _argumentTypes.get(i);
				Type callertype = callertypes.get(i);
				
				callertype.unifyWith(node, methodtype);
			}
		}
		catch (TypesNotCompatibleException e)
		{
			return false;
		}
		
		return true;
	}
	
	public void typeCheck(MethodCallNode node, List<Type> callertypes)
		throws CompilationException
	{
		if (!typeCheckImpl(node, callertypes))
			throw new MethodParameterMismatch(node, this, _argumentTypes, callertypes);
	}
}
