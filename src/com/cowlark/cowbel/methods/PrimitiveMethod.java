/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING.BSD in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel.methods;

import java.util.HashMap;
import com.cowlark.cowbel.instructions.MethodCallInstruction;

public abstract class PrimitiveMethod extends Method
{

	private static HashMap<String, Method> _primitiveMethods;

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
    }

	public static Method lookupPrimitiveMethod(String signature)
    {
    	return _primitiveMethods.get(signature);
    }

	public static void registerPrimitiveMethod(PrimitiveMethod method)
    {
    	_primitiveMethods.put(method.getSignature(), method);
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
