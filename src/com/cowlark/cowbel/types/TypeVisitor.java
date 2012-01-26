package com.cowlark.cowbel.types;

public abstract class TypeVisitor
{
	public abstract void visit(ArrayType type);
	public abstract void visit(BooleanType type);
	public abstract void visit(FunctionType type);
	public abstract void visit(IntegerType type);
	public abstract void visit(StringType type);
}
