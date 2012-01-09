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
	private static HashMap<String, TemplatedMethod.Factory> _typeFamilyTemplates;
	private static HashMap<Type, HashMap<String, Method>> _typeFamilyMethods;
	
	static
	{
		_primitiveMethods = new HashMap<String, Method>();
		registerPrimitiveMethod(new StringEqualsMethod());
		registerPrimitiveMethod(new StringAddMethod());
		registerPrimitiveMethod(new StringReplaceMethod());
		registerPrimitiveMethod(new StringPrintMethod());
		registerPrimitiveMethod(new BooleanToStringMethod());
		registerPrimitiveMethod(new BooleanNotMethod());
		registerPrimitiveMethod(new BooleanAndMethod());
		registerPrimitiveMethod(new BooleanOrMethod());
		registerPrimitiveMethod(new BooleanXorMethod());
		registerPrimitiveMethod(new IntegerEqualsMethod());
		registerPrimitiveMethod(new IntegerNotEqualsMethod());
		registerPrimitiveMethod(new IntegerGreaterThanMethod());
		registerPrimitiveMethod(new IntegerLessThanMethod());
		registerPrimitiveMethod(new IntegerGreaterThanOrEqualsMethod());
		registerPrimitiveMethod(new IntegerLessThanOrEqualsMethod());
		registerPrimitiveMethod(new IntegerNegateMethod());
		registerPrimitiveMethod(new IntegerAddMethod());
		registerPrimitiveMethod(new IntegerSubMethod());
		registerPrimitiveMethod(new IntegerToStringMethod());

		_typeFamilyTemplates = new HashMap<String, TemplatedMethod.Factory>();
		registerTemplatedMethodFactory(new ArraySizeMethod.Factory());
		registerTemplatedMethodFactory(new ArrayGetMethod.Factory());
		registerTemplatedMethodFactory(new ArraySetMethod.Factory());
		
		_typeFamilyMethods = new HashMap<Type, HashMap<String, Method>>();
	}
	
	public static Method lookupPrimitiveMethod(String signature)
	{
		return _primitiveMethods.get(signature);
	}
	
	public static void registerPrimitiveMethod(Method method)
	{
		_primitiveMethods.put(method.getSignature(), method);
	}
	
	public static Method lookupTypeFamilyMethod(Type type, String signature)
	{
		HashMap<String, Method> catalogue = _typeFamilyMethods.get(type);
		if (catalogue == null)
		{
			catalogue = new HashMap<String, Method>();
			_typeFamilyMethods.put(type, catalogue);
		}
		
		Method method = catalogue.get(signature);
		if (method == null)
		{
			TemplatedMethod.Factory f = _typeFamilyTemplates.get(signature);
			if (f == null)
				return null;
			
			method = f.create(type);
			catalogue.put(signature, method);
		}
		
		return method;
	}
	
	public static void registerTemplatedMethodFactory(TemplatedMethod.Factory factory)
	{
		_typeFamilyTemplates.put(factory.getSignature(), factory);
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
