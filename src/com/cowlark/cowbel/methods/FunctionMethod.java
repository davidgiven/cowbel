/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING.BSD in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel.methods;

import com.cowlark.cowbel.Function;
import com.cowlark.cowbel.instructions.MethodCallInstruction;
import com.cowlark.cowbel.types.FunctionType;

public class FunctionMethod extends Method
{
	private Function _function;
	
	public FunctionMethod(Function function)
    {
		_function = function;
		
		FunctionType functiontype = function.getType();
		setInputTypes(functiontype.getInputArgumentTypes());
		setOutputTypes(functiontype.getOutputArgumentTypes());
    }
	
	public Function getFunction()
    {
	    return _function;
    }
	
	@Override
	public String getName()
	{
	    return _function.getSignature();
	}
	
	@Override
	public void visit(MethodCallInstruction insn, MethodVisitor visitor)
	{
		visitor.visit(insn, this);
	}
}
