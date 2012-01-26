/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel.types;

public abstract class TypeVisitor
{
	public abstract void visit(ArrayType type);
	public abstract void visit(BooleanType type);
	public abstract void visit(FunctionType type);
	public abstract void visit(IntegerType type);
	public abstract void visit(StringType type);
}
