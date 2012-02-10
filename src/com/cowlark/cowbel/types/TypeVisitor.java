/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING.BSD in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel.types;

public abstract class TypeVisitor
{
	public abstract void visit(ExternType type);
	public abstract void visit(BooleanType type);
	public abstract void visit(FunctionType type);
	public abstract void visit(IntegerType type);
	public abstract void visit(RealType type);
	public abstract void visit(StringType type);
	public abstract void visit(ClassType type);
	public abstract void visit(InterfaceType type);
}
