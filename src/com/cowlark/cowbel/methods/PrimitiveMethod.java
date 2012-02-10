/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING.BSD in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel.methods;

import java.util.HashMap;
import com.cowlark.cowbel.instructions.MethodCallInstruction;
import com.cowlark.cowbel.types.BooleanType;
import com.cowlark.cowbel.types.IntegerType;
import com.cowlark.cowbel.types.StringType;

public abstract class PrimitiveMethod extends Method
{

	private static HashMap<String, Method> _primitiveMethods;

	static
    {
    	_primitiveMethods = new HashMap<String, Method>();
    	
    	registerPrimitiveMethod(new PrimitiveMethod()
		{
            {
    			setSignature("string.==.1", "string._eq");
    			setOutputTypes(BooleanType.create());
    			setInputTypes(StringType.create());
            }
		});
    	
    	registerPrimitiveMethod(new PrimitiveMethod()
		{
            {
    			setSignature("string.!=.1", "string._ne");
    			setOutputTypes(BooleanType.create());
    			setInputTypes(StringType.create());
            }
		});
    	
    	registerPrimitiveMethod(new PrimitiveMethod()
		{
            {
    			setSignature("string.+.1", "string._add");
    			setOutputTypes(StringType.create());
    			setInputTypes(StringType.create());
            }
		});
    	
    	/************************************************************/
    	/*                          BOOLEANS                        */
    	/************************************************************/

    	registerPrimitiveMethod(new PrimitiveMethod()
		{
            {
    			setSignature("boolean.==.1", "boolean._eq");
    			setOutputTypes(BooleanType.create());
    			setInputTypes(BooleanType.create());
            }
		});
    	
    	registerPrimitiveMethod(new PrimitiveMethod()
		{
            {
    			setSignature("boolean.!=.1", "boolean._ne");
    			setOutputTypes(BooleanType.create());
    			setInputTypes(BooleanType.create());
            }
		});
    	
    	registerPrimitiveMethod(new PrimitiveMethod()
		{
            {
    			setSignature("boolean.!.0", "boolean._not");
    			setOutputTypes(BooleanType.create());
    			setInputTypes();
            }
		});
    	
    	registerPrimitiveMethod(new PrimitiveMethod()
		{
            {
    			setSignature("boolean.&.1", "boolean._and");
    			setOutputTypes(BooleanType.create());
    			setInputTypes(BooleanType.create());
            }
		});
    	
    	registerPrimitiveMethod(new PrimitiveMethod()
		{
            {
    			setSignature("boolean.!.1", "boolean._or");
    			setOutputTypes(BooleanType.create());
    			setInputTypes(BooleanType.create());
            }
		});
    	
    	registerPrimitiveMethod(new PrimitiveMethod()
		{
            {
    			setSignature("boolean.^.1", "boolean._xor");
    			setOutputTypes(BooleanType.create());
    			setInputTypes(BooleanType.create());
            }
		});
    	
    	registerPrimitiveMethod(new PrimitiveMethod()
		{
            {
    			setSignature("boolean.toString.0", "boolean.toString");
    			setOutputTypes(StringType.create());
    			setInputTypes();
            }
		});
    	
    	/************************************************************/
    	/*                          INTEGERS                        */
    	/************************************************************/

    	registerPrimitiveMethod(new PrimitiveMethod()
		{
            {
    			setSignature("integer.==.1", "integer._eq");
    			setOutputTypes(BooleanType.create());
    			setInputTypes(IntegerType.create());
            }
		});
    	
    	registerPrimitiveMethod(new PrimitiveMethod()
		{
            {
    			setSignature("integer.!=.1", "integer._ne");
    			setOutputTypes(BooleanType.create());
    			setInputTypes(IntegerType.create());
            }
		});
    	
    	registerPrimitiveMethod(new PrimitiveMethod()
		{
            {
    			setSignature("integer.<.1", "integer._lt");
    			setOutputTypes(BooleanType.create());
    			setInputTypes(IntegerType.create());
            }
		});
    	
    	registerPrimitiveMethod(new PrimitiveMethod()
		{
            {
    			setSignature("integer.<=.1", "integer._le");
    			setOutputTypes(BooleanType.create());
    			setInputTypes(IntegerType.create());
            }
		});
    	
    	registerPrimitiveMethod(new PrimitiveMethod()
		{
            {
    			setSignature("integer.>.1", "integer._gt");
    			setOutputTypes(BooleanType.create());
    			setInputTypes(IntegerType.create());
            }
		});
    	
    	registerPrimitiveMethod(new PrimitiveMethod()
		{
            {
    			setSignature("integer.>=.1", "integer._ge");
    			setOutputTypes(BooleanType.create());
    			setInputTypes(IntegerType.create());
            }
		});
    	
    	registerPrimitiveMethod(new PrimitiveMethod()
		{
            {
    			setSignature("integer.+.1", "integer._add");
    			setOutputTypes(IntegerType.create());
    			setInputTypes(IntegerType.create());
            }
		});
    	
    	registerPrimitiveMethod(new PrimitiveMethod()
		{
            {
    			setSignature("integer.-.1", "integer._sub");
    			setOutputTypes(IntegerType.create());
    			setInputTypes(IntegerType.create());
            }
		});
    	
    	registerPrimitiveMethod(new PrimitiveMethod()
		{
            {
    			setSignature("integer.-.0", "integer._negate");
    			setOutputTypes(IntegerType.create());
    			setInputTypes(IntegerType.create());
            }
		});
    	
    	registerPrimitiveMethod(new PrimitiveMethod()
		{
            {
    			setSignature("integer.*.1", "integer._multiply");
    			setOutputTypes(IntegerType.create());
    			setInputTypes(IntegerType.create());
            }
		});
    	
    	registerPrimitiveMethod(new PrimitiveMethod()
		{
            {
    			setSignature("integer./.1", "integer._divide");
    			setOutputTypes(IntegerType.create());
    			setInputTypes(IntegerType.create());
            }
		});
    	
    	registerPrimitiveMethod(new PrimitiveMethod()
		{
            {
    			setSignature("integer.%.1", "integer._modulus");
    			setOutputTypes(IntegerType.create());
    			setInputTypes(IntegerType.create());
            }
		});
    	
    	registerPrimitiveMethod(new PrimitiveMethod()
		{
            {
    			setSignature("integer.toString.0", "integer.toString");
    			setOutputTypes(StringType.create());
    			setInputTypes();
            }
		});
    	
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
