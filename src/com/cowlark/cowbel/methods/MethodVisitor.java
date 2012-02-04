/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING.BSD in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel.methods;

import com.cowlark.cowbel.instructions.MethodCallInstruction;

public interface MethodVisitor
{
	public void visit(MethodCallInstruction insn, FunctionMethod method);
	public void visit(MethodCallInstruction insn, PrimitiveMethod method);
}
