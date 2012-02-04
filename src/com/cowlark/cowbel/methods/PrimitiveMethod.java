/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING.BSD in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel.methods;

import java.util.HashMap;
import com.cowlark.cowbel.instructions.MethodCallInstruction;
import com.cowlark.cowbel.types.Type;

public abstract class PrimitiveMethod extends Method
{

	private static HashMap<String, Method> _primitiveMethods;
	private static HashMap<Type, HashMap<String, Method>> _typeFamilyMethods;
	private static HashMap<String, TemplatedMethod.Factory> _typeFamilyTemplates;

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
    	registerTemplatedMethodFactory(new ArrayResizeMethod.Factory());
    	registerTemplatedMethodFactory(new ArraySizeMethod.Factory());
    	registerTemplatedMethodFactory(new ArrayGetMethod.Factory());
    	registerTemplatedMethodFactory(new ArraySetMethod.Factory());
    	
    	_typeFamilyMethods = new HashMap<Type, HashMap<String, Method>>();
    }

	public static Method lookupPrimitiveMethod(String signature)
    {
    	return _primitiveMethods.get(signature);
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

	public static void registerPrimitiveMethod(PrimitiveMethod method)
    {
    	_primitiveMethods.put(method.getSignature(), method);
    }

	public static void registerTemplatedMethodFactory(TemplatedMethod.Factory factory)
    {
    	_typeFamilyTemplates.put(factory.getSignature(), factory);
    }

	private String _identifier;
	private String _signature;

	public String getSignature()
    {
    	return _signature;
    }

	public String getIdentifier()
	{
		return _identifier;
	}
	
	@Override
	public String getName()
	{
	    return getSignature();
	}
	
	protected void setSignature(String signature, String identifier)
    {
    	_signature = signature;
    	_identifier = identifier;
    }
	
	@Override
	public void visit(MethodCallInstruction insn, MethodVisitor visitor)
	{
		visitor.visit(insn, this);
	}
}
