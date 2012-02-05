/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING.BSD in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel.methods;

import com.cowlark.cowbel.TypeContext;
import com.cowlark.cowbel.instructions.MethodCallInstruction;

public class VirtualMethod extends Method
{
	private com.cowlark.cowbel.MethodTemplate _template;
	
	public VirtualMethod(com.cowlark.cowbel.MethodTemplate template, TypeContext typecontext)
    {
		_template = template;
		
//		FunctionType functiontype = function.getType();
//		setInputTypes(functiontype.getInputArgumentTypes());
//		setOutputTypes(functiontype.getOutputArgumentTypes());
    }

	public com.cowlark.cowbel.MethodTemplate getTemplate()
    {
	    return _template;
    }
	
	@Override
	public String getName()
	{
	    return _template.getSignature();
	}
	
	@Override
	public void visit(MethodCallInstruction insn, MethodVisitor visitor)
	{
		visitor.visit(insn, this);
	}
}
