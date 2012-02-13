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
import com.cowlark.cowbel.types.RealType;
import com.cowlark.cowbel.types.StringType;

public abstract class PrimitiveMethod extends Method
{

	private static HashMap<String, Method> _primitiveMethods;

	static
    {
    	_primitiveMethods = new HashMap<String, Method>();
    	
    	/************************************************************/
    	/*                           STRINGS                        */
    	/************************************************************/

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
    			setSignature("string.<.1", "string._lt");
    			setOutputTypes(BooleanType.create());
    			setInputTypes(StringType.create());
            }
		});
    	
    	registerPrimitiveMethod(new PrimitiveMethod()
		{
            {
    			setSignature("string.>.1", "string._gt");
    			setOutputTypes(BooleanType.create());
    			setInputTypes(StringType.create());
            }
		});
    	
    	registerPrimitiveMethod(new PrimitiveMethod()
		{
            {
    			setSignature("string.<=.1", "string._le");
    			setOutputTypes(BooleanType.create());
    			setInputTypes(StringType.create());
            }
		});
    	
    	registerPrimitiveMethod(new PrimitiveMethod()
		{
            {
    			setSignature("string.>=.1", "string._ge");
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
    			setSignature("boolean.|.1", "boolean._or");
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
    			setSignature("int.==.1", "int._eq");
    			setOutputTypes(BooleanType.create());
    			setInputTypes(IntegerType.create());
            }
		});
    	
    	registerPrimitiveMethod(new PrimitiveMethod()
		{
            {
    			setSignature("int.!=.1", "int._ne");
    			setOutputTypes(BooleanType.create());
    			setInputTypes(IntegerType.create());
            }
		});
    	
    	registerPrimitiveMethod(new PrimitiveMethod()
		{
            {
    			setSignature("int.<.1", "int._lt");
    			setOutputTypes(BooleanType.create());
    			setInputTypes(IntegerType.create());
            }
		});
    	
    	registerPrimitiveMethod(new PrimitiveMethod()
		{
            {
    			setSignature("int.<=.1", "int._le");
    			setOutputTypes(BooleanType.create());
    			setInputTypes(IntegerType.create());
            }
		});
    	
    	registerPrimitiveMethod(new PrimitiveMethod()
		{
            {
    			setSignature("int.>.1", "int._gt");
    			setOutputTypes(BooleanType.create());
    			setInputTypes(IntegerType.create());
            }
		});
    	
    	registerPrimitiveMethod(new PrimitiveMethod()
		{
            {
    			setSignature("int.>=.1", "int._ge");
    			setOutputTypes(BooleanType.create());
    			setInputTypes(IntegerType.create());
            }
		});
    	
    	registerPrimitiveMethod(new PrimitiveMethod()
		{
            {
    			setSignature("int.+.1", "int._add");
    			setOutputTypes(IntegerType.create());
    			setInputTypes(IntegerType.create());
            }
		});
    	
    	registerPrimitiveMethod(new PrimitiveMethod()
		{
            {
    			setSignature("int.-.1", "int._sub");
    			setOutputTypes(IntegerType.create());
    			setInputTypes(IntegerType.create());
            }
		});
    	
    	registerPrimitiveMethod(new PrimitiveMethod()
		{
            {
    			setSignature("int.-.0", "int._negate");
    			setOutputTypes(IntegerType.create());
    			setInputTypes(IntegerType.create());
            }
		});
    	
    	registerPrimitiveMethod(new PrimitiveMethod()
		{
            {
    			setSignature("int.*.1", "int._multiply");
    			setOutputTypes(IntegerType.create());
    			setInputTypes(IntegerType.create());
            }
		});
    	
    	registerPrimitiveMethod(new PrimitiveMethod()
		{
            {
    			setSignature("int./.1", "int._divide");
    			setOutputTypes(IntegerType.create());
    			setInputTypes(IntegerType.create());
            }
		});
    	
    	registerPrimitiveMethod(new PrimitiveMethod()
		{
            {
    			setSignature("int.%.1", "int._modulus");
    			setOutputTypes(IntegerType.create());
    			setInputTypes(IntegerType.create());
            }
		});
    	
    	registerPrimitiveMethod(new PrimitiveMethod()
		{
            {
    			setSignature("int.toString.0", "int.toString");
    			setOutputTypes(StringType.create());
    			setInputTypes();
            }
		});
    	
    	registerPrimitiveMethod(new PrimitiveMethod()
		{
            {
    			setSignature("int.toReal.0", "int.toReal");
    			setOutputTypes(RealType.create());
    			setInputTypes();
            }
		});
    	
    	registerPrimitiveMethod(new PrimitiveMethod()
		{
            {
    			setSignature("int.<<.1", "int._shl");
    			setOutputTypes(IntegerType.create());
    			setInputTypes(IntegerType.create());
            }
		});
    	
    	registerPrimitiveMethod(new PrimitiveMethod()
		{
            {
    			setSignature("int.>>.1", "int._shr");
    			setOutputTypes(IntegerType.create());
    			setInputTypes(IntegerType.create());
            }
		});
    	
    	/************************************************************/
    	/*                           REALS                          */
    	/************************************************************/

    	registerPrimitiveMethod(new PrimitiveMethod()
		{
            {
    			setSignature("real.==.1", "real._eq");
    			setOutputTypes(BooleanType.create());
    			setInputTypes(RealType.create());
            }
		});
    	
    	registerPrimitiveMethod(new PrimitiveMethod()
		{
            {
    			setSignature("real.!=.1", "real._ne");
    			setOutputTypes(BooleanType.create());
    			setInputTypes(RealType.create());
            }
		});
    	
    	registerPrimitiveMethod(new PrimitiveMethod()
		{
            {
    			setSignature("real.<.1", "real._lt");
    			setOutputTypes(BooleanType.create());
    			setInputTypes(RealType.create());
            }
		});
    	
    	registerPrimitiveMethod(new PrimitiveMethod()
		{
            {
    			setSignature("real.<=.1", "real._le");
    			setOutputTypes(BooleanType.create());
    			setInputTypes(RealType.create());
            }
		});
    	
    	registerPrimitiveMethod(new PrimitiveMethod()
		{
            {
    			setSignature("real.>.1", "real._gt");
    			setOutputTypes(BooleanType.create());
    			setInputTypes(RealType.create());
            }
		});
    	
    	registerPrimitiveMethod(new PrimitiveMethod()
		{
            {
    			setSignature("real.>=.1", "real._ge");
    			setOutputTypes(BooleanType.create());
    			setInputTypes(RealType.create());
            }
		});
    	
    	registerPrimitiveMethod(new PrimitiveMethod()
		{
            {
    			setSignature("real.+.1", "real._add");
    			setOutputTypes(RealType.create());
    			setInputTypes(RealType.create());
            }
		});
    	
    	registerPrimitiveMethod(new PrimitiveMethod()
		{
            {
    			setSignature("real.-.1", "real._sub");
    			setOutputTypes(RealType.create());
    			setInputTypes(RealType.create());
            }
		});
    	
    	registerPrimitiveMethod(new PrimitiveMethod()
		{
            {
    			setSignature("real.-.0", "real._negate");
    			setOutputTypes(RealType.create());
    			setInputTypes(RealType.create());
            }
		});
    	
    	registerPrimitiveMethod(new PrimitiveMethod()
		{
            {
    			setSignature("real.*.1", "real._multiply");
    			setOutputTypes(RealType.create());
    			setInputTypes(RealType.create());
            }
		});
    	
    	registerPrimitiveMethod(new PrimitiveMethod()
		{
            {
    			setSignature("real./.1", "real._divide");
    			setOutputTypes(RealType.create());
    			setInputTypes(RealType.create());
            }
		});
    	
    	registerPrimitiveMethod(new PrimitiveMethod()
		{
            {
    			setSignature("real.toString.0", "real.toString");
    			setOutputTypes(StringType.create());
    			setInputTypes();
            }
		});
    	
    	registerPrimitiveMethod(new PrimitiveMethod()
		{
            {
    			setSignature("real.toInt.0", "real.toInt");
    			setOutputTypes(IntegerType.create());
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
